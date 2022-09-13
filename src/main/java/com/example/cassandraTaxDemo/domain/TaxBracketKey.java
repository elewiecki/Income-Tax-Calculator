package com.example.cassandraTaxDemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@PrimaryKeyClass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxBracketKey {

    @JsonProperty
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private double taxRate;

    @JsonProperty
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private UUID id;
}
