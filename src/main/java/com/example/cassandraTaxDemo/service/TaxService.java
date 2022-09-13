package com.example.cassandraTaxDemo.service;

import com.example.cassandraTaxDemo.domain.*;
import com.example.cassandraTaxDemo.Repository.TaxBracketRepository;
import com.example.cassandraTaxDemo.Repository.TaxResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class TaxService {

    private TaxBracketRepository taxBracketRepo;
    private TaxResponseRepository taxResponseRepo;


    public TaxService(@Autowired TaxBracketRepository taxBracketRepo,
                      @Autowired TaxResponseRepository taxResponseRepo){
        this.taxBracketRepo = taxBracketRepo;
        this.taxResponseRepo = taxResponseRepo;
    }

    //calculates taxes in a single bracket based un salary, bounds, and rate
    //returns: taxes to be removed from this bracket
    private double calculateTaxesInBracket(double salary, double lowerBound, double upperBound, double taxRate){
        double earnedInBracket;
        if(upperBound == -1 || salary < upperBound){
            earnedInBracket = salary - lowerBound;
        }
        else earnedInBracket = upperBound - lowerBound;
        return  earnedInBracket * taxRate;
    }

    //calculates total taxes from a request. Also finds highest tax bracket based on salary and rates.
    //configures response using setters and returns response
    public EmployeeTaxResponse calculateTaxes(EmployeeTaxRequest employeeTaxRequest){
        EmployeeTaxResponse response = new EmployeeTaxResponse();
        List<TaxBracket> taxBrackets = taxBracketRepo.findAll();
        double cumulativeTax = 0;
        TaxBracket highestBracket = null;
        for(int i = 0; i < taxBrackets.size(); ++i){ //loop through all tax brackets
            TaxBracket taxBracket = taxBrackets.get(i);
            TaxBracketKey key = taxBracket.getKey();
            double taxesRemovedThisBracket = 0;
            if(employeeTaxRequest.isMarried() && employeeTaxRequest.getSalary() > taxBracket.getLowerBoundMarried()){ //do the same calculations based on isMarried
                taxesRemovedThisBracket = calculateTaxesInBracket(employeeTaxRequest.getSalary(),
                        taxBracket.getLowerBoundMarried(), taxBracket.getUpperBoundMarried(), key.getTaxRate());
            }
            else if(!employeeTaxRequest.isMarried() && employeeTaxRequest.getSalary() > taxBracket.getLowerBoundSingle()){
                taxesRemovedThisBracket = calculateTaxesInBracket(employeeTaxRequest.getSalary(),
                        taxBracket.getLowerBoundSingle(), taxBracket.getUpperBoundSingle(), key.getTaxRate());
            }
            if((highestBracket == null || key.getTaxRate() > highestBracket.getKey().getTaxRate()) && taxesRemovedThisBracket > 0){ //find highest tax bracket employee falls into
                highestBracket = taxBracket;
            }
            cumulativeTax += taxesRemovedThisBracket;
        }
        //configure response based on calculation
        ResponseKey responseKey = new ResponseKey(employeeTaxRequest.getId(), highestBracket.getKey().getId());
        response.setSalary(employeeTaxRequest.getSalary());
        response.setTaxesRemoved(cumulativeTax);
        response.setNetIncome(employeeTaxRequest.getSalary() - cumulativeTax);
        response.setKey(responseKey);
        response.setName(employeeTaxRequest.getName());
        taxResponseRepo.save(response); //add response to database
        return response;
    }

    //First finds an existing response by ID, if there is none it creates a new one.
    //If one does exists, it is deleted and replaced
    public EmployeeTaxResponse calculateTaxesById(EmployeeTaxRequest employeeTaxRequest, UUID id){
        Optional<EmployeeTaxResponse> existingResponse = getResponseById(id);
        if (existingResponse.isPresent()) {
            deleteResponseById(id);
        }
        employeeTaxRequest.setId(id);
        return calculateTaxes(employeeTaxRequest);
    }

    //add array of tax brackets to the database
    public List<TaxBracket> setTaxBrackets(TaxBracketRequest[] newTaxBrackets){
        taxBracketRepo.deleteAll();
        //checks to make sure first bracket starts at 0 and last bracket has no end (-1)
        if(newTaxBrackets[0].getLowerBoundSingle() != 0
                || newTaxBrackets[0].getLowerBoundMarried() != 0
                || newTaxBrackets[newTaxBrackets.length - 1].getUpperBoundSingle() != -1
                || newTaxBrackets[newTaxBrackets.length - 1].getUpperBoundMarried() != -1){
            return taxBracketRepo.findAll();
        }

        //Make sure that lower bound is always less than upper bound,
        //and that the current lower bound is greater than the previous upper bound
        double prevMaxSingle = newTaxBrackets[0].getUpperBoundSingle();
        double prevMaxMarried = newTaxBrackets[0].getUpperBoundMarried();
        for(int i = 1; i < newTaxBrackets.length - 1; ++i){
            //lower is less than upper
            if (newTaxBrackets[i].getUpperBoundMarried() < newTaxBrackets[i].getLowerBoundMarried()
                    || newTaxBrackets[i].getUpperBoundSingle() < newTaxBrackets[i].getLowerBoundSingle()){
                return taxBracketRepo.findAll();
            }
            //current lower is greater than previous upper
            else if (newTaxBrackets[i].getLowerBoundSingle() < prevMaxSingle
                    || newTaxBrackets[i].getLowerBoundMarried() < prevMaxMarried) {
                return taxBracketRepo.findAll();
            }
        }
        //update brackets
        for(TaxBracketRequest bracket : newTaxBrackets){
            TaxBracket newBracket = new TaxBracket(bracket.getLowerBoundSingle(), bracket.getUpperBoundSingle(),
                    bracket.getLowerBoundMarried(), bracket.getUpperBoundMarried(),
                    new TaxBracketKey(bracket.getTaxRate(), UUID.randomUUID()));
            taxBracketRepo.save(newBracket);
        }
        return taxBracketRepo.findAll();
    }


    //returns all tax brackets in database
    public List<TaxBracket> getAllTaxBrackets(){
        return taxBracketRepo.findAll();
    }

    public void deleteTaxBrackets(){
        taxBracketRepo.deleteAll();
    }

    public void deleteAllTaxResponses(){
        taxResponseRepo.deleteAll();
    }

    //returns all responses in database
    public List<EmployeeTaxResponse> getAllEmployeeTaxResponses(){
        return taxResponseRepo.findAll();
    }

    //returns a specific response based on id
    public Optional<EmployeeTaxResponse> getResponseById(UUID id){
        return taxResponseRepo.findByKeyId(id);
    }

    //deletes a response based on id
    public void deleteResponseById(UUID id){
        taxResponseRepo.deleteByKeyId(id);
    }

    public List<EmployeeTaxResponse> getResponsesByHighestTaxBracket( UUID id){
        return taxResponseRepo.findByKeyHighestTaxBracket(id);
    }

}
