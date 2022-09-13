package com.example.cassandraTaxDemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.UUID;

//class representing an individual tax bracket.
//contains a rate, upper and lower bound for single and married employees

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class TaxBracket {

    @JsonProperty("lowerBoundSingle")
    private double lowerBoundSingle = 0;
    @JsonProperty("upperBoundSingle")
    private double upperBoundSingle = -1;
    @JsonProperty("lowerBoundMarried")
    private double lowerBoundMarried = 0;
    @JsonProperty("upperBoundMarried")
    private double upperBoundMarried = -1;

    @JsonProperty
    @PrimaryKey
    private TaxBracketKey key;

    public TaxBracketKey getKey() {
        return key;
    }

    public void setKey(TaxBracketKey key) {
        this.key = key;
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
