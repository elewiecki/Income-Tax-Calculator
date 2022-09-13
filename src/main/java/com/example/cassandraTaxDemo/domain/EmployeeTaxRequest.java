package com.example.cassandraTaxDemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.util.UUID;

//class representing all information needed to submit a tax calculation request:
//employees name, id, salary, and weather or not they are married
public class EmployeeTaxRequest {

    @JsonProperty("name")
    private String name = "";
    @JsonProperty("salary")
    private double salary = -1;
    @JsonProperty("isMarried")
    private boolean isMarried = false;
    @JsonProperty("id")
    @PrimaryKey
    private UUID id;

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
