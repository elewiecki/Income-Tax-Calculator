package com.example.cassandraTaxDemo;

import com.example.cassandraTaxDemo.domain.EmployeeTaxRequest;
import com.example.cassandraTaxDemo.domain.EmployeeTaxResponse;
import com.example.cassandraTaxDemo.domain.TaxBracket;
import com.example.cassandraTaxDemo.domain.TaxBracketRequest;
import com.example.cassandraTaxDemo.service.TaxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class EmployeeTaxCalculatorApplicationTests {

	TaxService service;

	@Autowired
	public EmployeeTaxCalculatorApplicationTests(TaxService taxService){
		this.service = taxService;
	}

	//Test the program's ability to save tax brackets in memory, in the order given
	//Creates two new tax brackets, then retrieves the stored data to confirm the brackets
	//Still exist and the first on in memory is the first one given
	@Test
	public void testTaxBracketSaving(){
		service.deleteTaxBrackets();
		service.deleteAllTaxResponses();
		TaxBracketRequest newBracket1 = new TaxBracketRequest();
		newBracket1.setTaxRate(0.10);
		newBracket1.setLowerBoundSingle(0);
		newBracket1.setUpperBoundSingle(1000);
		newBracket1.setLowerBoundMarried(0);
		newBracket1.setUpperBoundMarried(2000);
		TaxBracketRequest newBracket2 = new TaxBracketRequest();
		newBracket2.setTaxRate(0.20);
		newBracket2.setLowerBoundSingle(1001);
		newBracket2.setLowerBoundMarried(2001);
		TaxBracketRequest[] newBrackets = {newBracket1, newBracket2};
		service.setTaxBrackets(newBrackets);
		assert service.getAllTaxBrackets().size() == 2;
	}

	//Tests the program's ability to recognize invalid tax brackets and not store them,
	//instead throwing an exception
	@Test
	public void testInvalidTaxBracket(){
		service.deleteTaxBrackets();
		service.deleteAllTaxResponses();
		//INVALID: First bracket does not start at 0
		TaxBracketRequest newBracket1 = new TaxBracketRequest();
		newBracket1.setTaxRate(0.10);
		newBracket1.setLowerBoundSingle(1);
		newBracket1.setUpperBoundSingle(1000);
		newBracket1.setLowerBoundMarried(0);
		newBracket1.setUpperBoundMarried(2000);

		TaxBracketRequest newBracket2 = new TaxBracketRequest();
		newBracket2.setTaxRate(0.20);
		newBracket2.setLowerBoundSingle(1001);
		newBracket2.setLowerBoundMarried(2001);
		newBracket2.setUpperBoundSingle(2000);
		newBracket2.setUpperBoundMarried(3000);

		TaxBracketRequest newBracket3 = new TaxBracketRequest();
		newBracket3.setTaxRate(0.20);
		newBracket3.setLowerBoundSingle(10000);
		newBracket3.setLowerBoundMarried(10000);

		//With first bracket invalid, program does not store tax brackets
		TaxBracketRequest[] newBrackets = {newBracket1, newBracket2, newBracket3};
		service.setTaxBrackets(newBrackets);
		assert service.getAllTaxBrackets().size() == 0;

		//Validates first bracket, and confirms that they can now be added
		newBracket1.setLowerBoundSingle(0);
		service.setTaxBrackets(newBrackets);
		assert service.getAllTaxBrackets().size() == 3;

		//invalidates third bracket. Tries to add it, makes sure nothing happens
		service.deleteTaxBrackets();
		newBracket3.setUpperBoundSingle(1);
		service.setTaxBrackets(newBrackets);
		assert  service.getAllTaxBrackets().size() == 0;
	}

	//Tests program's tax calculation against calculations done by hand.
	//Creates multiple brackets, submits a request, then compares the response to expected outcome
	@Test
	public void testTaxCalculation(){
		service.deleteTaxBrackets();
		service.deleteAllTaxResponses(); //deletes any existing brackets to start from scratch
		TaxBracketRequest newBracket1 = new TaxBracketRequest();
		newBracket1.setTaxRate(0.10);
		newBracket1.setLowerBoundSingle(0);
		newBracket1.setUpperBoundSingle(1000);
		newBracket1.setLowerBoundMarried(0);
		newBracket1.setUpperBoundMarried(2000);
		TaxBracketRequest newBracket2 = new TaxBracketRequest();
		newBracket2.setTaxRate(0.20);
		newBracket2.setLowerBoundSingle(1001);
		newBracket2.setLowerBoundMarried(2001);
		TaxBracketRequest[] newBrackets = {newBracket1, newBracket2};
		service.setTaxBrackets(newBrackets);

		EmployeeTaxRequest request = new EmployeeTaxRequest();
		request.setName("john");
		UUID id = UUID.randomUUID();
		request.setId(id);
		request.setSalary(3000);
		request.setMarried(false);

		//compares program's calculations to hand calculations
		EmployeeTaxResponse response = service.calculateTaxes(request);
		assert response.getKey().getId() == id;
		assert response.getTaxesRemoved() == 499.8;
		assert response.getNetIncome() == 3000 - 499.8;

		//does the same with variables changed
		request.setMarried(true);
		id = UUID.randomUUID();
		request.setId(id);
		response = service.calculateTaxes(request);
		assert response.getKey().getId() == id;
		assert response.getTaxesRemoved() == 399.8;
		assert response.getNetIncome() == 3000 - 399.8;
	}

	//Compares response to expected when the salary is outside the bounds of the largest tax bracket
	@Test
	public void testTaxCalculationSalaryOutsideBounds(){
		service.deleteTaxBrackets();
		service.deleteAllTaxResponses(); //deletes any existing brackets to start from scratch
		TaxBracketRequest newBracket1 = new TaxBracketRequest();
		newBracket1.setTaxRate(0.10);
		newBracket1.setLowerBoundSingle(0);
		newBracket1.setUpperBoundSingle(1000);
		newBracket1.setLowerBoundMarried(0);
		newBracket1.setUpperBoundMarried(2000);
		TaxBracketRequest newBracket2 = new TaxBracketRequest();
		newBracket2.setTaxRate(0.20);
		newBracket2.setLowerBoundSingle(1001);
		newBracket2.setLowerBoundMarried(2001);
		TaxBracketRequest[] newBrackets = {newBracket1, newBracket2};
		service.setTaxBrackets(newBrackets);

		EmployeeTaxRequest request = new EmployeeTaxRequest();
		request.setName("john");
		UUID id = UUID.randomUUID();
		request.setId(id);
		request.setSalary(3000);
		request.setMarried(false);

		//compares program's calculations to hand calculations
		EmployeeTaxResponse response = service.calculateTaxes(request);
		assert response.getKey().getId() == id;
		assert response.getTaxesRemoved() == 499.8;
		assert response.getNetIncome() == 3000 - 499.8;
	}



	//Tests program's ability to save responses in memory
	@Test
	public void testResponseSaving(){
		service.deleteTaxBrackets();
		service.deleteAllTaxResponses();
		TaxBracketRequest newBracket1 = new TaxBracketRequest();
		newBracket1.setTaxRate(0.10);
		newBracket1.setLowerBoundSingle(0);
		newBracket1.setUpperBoundSingle(1000);
		newBracket1.setLowerBoundMarried(0);
		newBracket1.setUpperBoundMarried(2000);
		TaxBracketRequest newBracket2 = new TaxBracketRequest();
		newBracket2.setTaxRate(0.20);
		newBracket2.setLowerBoundSingle(1001);
		newBracket2.setLowerBoundMarried(2001);
		TaxBracketRequest[] newBrackets = {newBracket1, newBracket2};
		service.setTaxBrackets(newBrackets);

		EmployeeTaxRequest request = new EmployeeTaxRequest();
		request.setName("john");
		UUID id = UUID.randomUUID();
		request.setId(id);
		request.setSalary(3000);
		request.setMarried(false);

		EmployeeTaxResponse response = service.calculateTaxes(request);
		List<EmployeeTaxResponse> employeeTaxResponses = service.getAllEmployeeTaxResponses();
		assert employeeTaxResponses.size() == 1;
		assert employeeTaxResponses.get(0).getKey().getId().equals(id);
		request.setMarried(true);
		id = UUID.randomUUID();
		request.setId(id);
		response = service.calculateTaxes(request);
		employeeTaxResponses = service.getAllEmployeeTaxResponses();
		assert employeeTaxResponses.size() == 2;
		//assert employeeTaxResponses.get(1).getKey().getId().equals(id);
	}



}
