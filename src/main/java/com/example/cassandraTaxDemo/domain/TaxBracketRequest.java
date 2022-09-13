package com.example.cassandraTaxDemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

public class TaxBracketRequest {

    @JsonProperty("lowerBoundSingle")
    private double lowerBoundSingle = 0;
    @JsonProperty("upperBoundSingle")
    private double upperBoundSingle = -1;
    @JsonProperty("lowerBoundMarried")
    private double lowerBoundMarried = 0;
    @JsonProperty("upperBoundMarried")
    private double upperBoundMarried = -1;

    @JsonProperty
    private double taxRate = 0;

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getLowerBoundSingle() {
        return lowerBoundSingle;
    }

    public void setLowerBoundSingle(double lowerBoundSingle) {
        this.lowerBoundSingle = lowerBoundSingle;
    }

    public double getUpperBoundSingle() {
        return upperBoundSingle;
    }

    public void setUpperBoundSingle(double upperBoundSingle) {
        this.upperBoundSingle = upperBoundSingle;
    }

    public double getLowerBoundMarried() {
        return lowerBoundMarried;
    }

    public void setLowerBoundMarried(double lowerBoundMarried) {
        this.lowerBoundMarried = lowerBoundMarried;
    }

    public double getUpperBoundMarried() {
        return upperBoundMarried;
    }

    public void setUpperBoundMarried(double upperBoundMarried) {
        this.upperBoundMarried = upperBoundMarried;
    }
}
