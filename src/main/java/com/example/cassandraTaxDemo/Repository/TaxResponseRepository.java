package com.example.cassandraTaxDemo.Repository;

import com.example.cassandraTaxDemo.domain.EmployeeTaxResponse;
import com.example.cassandraTaxDemo.domain.ResponseKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxResponseRepository extends CassandraRepository<EmployeeTaxResponse, ResponseKey> {

    //DONT DO THIS: ID IS NOT PRIMARY KEY (JUST HERE FOR TESTING)
    Optional<EmployeeTaxResponse> findByKeyId(UUID id);

    void deleteByKeyId(UUID id);

    List<EmployeeTaxResponse> findByKeyHighestTaxBracket(UUID id);

}
