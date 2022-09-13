package com.example.cassandraTaxDemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

//class returned after taxes are calculated based on request.
//represents an employee's salary after taxes are removed, plus additional information
@Table("responses_by_tax_bracket")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTaxResponse {

    @JsonProperty("name")
    private String name;
    @JsonProperty("salary")
    private double salary;

    @JsonProperty("taxesRemoved")
    private double taxesRemoved;
    @JsonProperty("netIncome")
    private double netIncome;

    @PrimaryKey
    private ResponseKey key;

    public ResponseKey getKey() {
        return key;
    }

    public void setKey(ResponseKey key) {
        this.key = key;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getTaxesRemoved() {
        return taxesRemoved;
    }

    public void setTaxesRemoved(double taxesRemoved) {
        this.taxesRemoved = taxesRemoved;
    }

    public double getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(double netIncome) {
        this.netIncome = netIncome;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
