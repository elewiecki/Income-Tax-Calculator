package com.example.cassandraTaxDemo.Repository;

import com.example.cassandraTaxDemo.domain.TaxBracket;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface TaxBracketRepository extends CassandraRepository<TaxBracket, UUID> {
}
