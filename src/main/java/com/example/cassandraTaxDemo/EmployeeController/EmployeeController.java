package com.example.cassandraTaxDemo.EmployeeController;

import com.example.cassandraTaxDemo.Exception.BadRequestException;
import com.example.cassandraTaxDemo.Exception.DoesNotExistException;
import com.example.cassandraTaxDemo.domain.EmployeeTaxRequest;
import com.example.cassandraTaxDemo.domain.EmployeeTaxResponse;
import com.example.cassandraTaxDemo.domain.TaxBracket;
import com.example.cassandraTaxDemo.domain.TaxBracketRequest;
import com.example.cassandraTaxDemo.service.TaxService;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/income")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    private TaxService taxService;

    @Autowired
    public EmployeeController(TaxService taxService){
        this.taxService = taxService;
    }

    @GetMapping(value = "instructions")
    public String getInstructions(){
        return "First, enter tax brackets as a JSON array: rate, lower and upper bound for single and married.\n" +
                "The first bracket must have lower bounds of 0, and the largest must not contain upper bounds.\n" +
                "Then, submit a request containing: name, salary, and isMarried. You will receive a response with an ID\n" +
                "Responses can be accessed, deleted, and re-requested later with this ID.";
    }

    @PostMapping
    public ResponseEntity<Object> requestNewTaxCalculation(@RequestBody EmployeeTaxRequest employeeTaxRequest){
        if(employeeTaxRequest.getName().isEmpty()){
            throw new BadRequestException("No employee name given");
        }
        else if(employeeTaxRequest.getSalary() < 0){
            throw new BadRequestException("Salary is invalid");
        }
        employeeTaxRequest.setId(UUID.randomUUID());
        return new ResponseEntity<>(taxService.calculateTaxes(employeeTaxRequest), HttpStatus.OK);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> requestNewTaxCalculationById(@RequestBody EmployeeTaxRequest employeeTaxRequest,
                                                               @PathVariable UUID id){
        if(employeeTaxRequest.getName().isEmpty()){
            throw new BadRequestException("No employee name given");
        }
        else if(employeeTaxRequest.getSalary() < 0){
            throw new BadRequestException("Salary is invalid");
        }
        return new ResponseEntity<>(taxService.calculateTaxesById(employeeTaxRequest, id), HttpStatus.OK);
    }

    @PostMapping(value = "tax-brackets")
    public List<TaxBracket> setTaxBrackets(@RequestBody TaxBracketRequest[] taxBrackets){
        List<TaxBracket> bracketsSet = taxService.setTaxBrackets(taxBrackets);
        if(bracketsSet.isEmpty()) throw new BadRequestException("Invalid Tax Brackets");
        return bracketsSet;
    }

    @GetMapping(value = "tax-brackets")
    public List<TaxBracket> getAllTaxBrackets(){
        List<TaxBracket> taxBrackets = taxService.getAllTaxBrackets();
        if(taxBrackets.size() == 0) throw new DoesNotExistException("No Tax Brackets Exist");
        return taxBrackets;
    }

    @DeleteMapping(value = "tax-brackets")
    public void deleteTaxBrackets(){
        taxService.deleteTaxBrackets();
    }

    @GetMapping
    public List<EmployeeTaxResponse> getAllEmployeeTaxResponses(){
        List<EmployeeTaxResponse> responses = taxService.getAllEmployeeTaxResponses();
        if(responses.isEmpty()) throw new DoesNotExistException("No Responses Exist");
        return responses;
    }

    @GetMapping(path = "{id}")
    public EmployeeTaxResponse getResponseById(@PathVariable("id") UUID id) {
        Optional<EmployeeTaxResponse> response = taxService.getResponseById(id);
        if (response.isEmpty()) throw new DoesNotExistException("Response does not exist");
        return response.get();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteEmployeeTaxResponseById(@PathVariable("id") UUID id){
        Optional<EmployeeTaxResponse> deletedResponse = taxService.getResponseById(id);
        if(deletedResponse.isEmpty()) throw new DoesNotExistException("Response does not exist, cannot be deleted");
        else taxService.deleteResponseById(id);
        return new ResponseEntity<>(deletedResponse.get(), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteAllResponses(){
        taxService.deleteAllTaxResponses();
    }

    @GetMapping(path = "tax-brackets/{id}")
    public List<EmployeeTaxResponse> getResponsesByHighestTaxBracket(@PathVariable UUID id){
        return taxService.getResponsesByHighestTaxBracket(id);
    }

}
