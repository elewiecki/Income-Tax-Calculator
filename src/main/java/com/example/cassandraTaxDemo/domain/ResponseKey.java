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
public class ResponseKey {

    @JsonProperty("id")
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private UUID id;

    @JsonProperty("highestTaxBracket")
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID highestTaxBracket;

}
