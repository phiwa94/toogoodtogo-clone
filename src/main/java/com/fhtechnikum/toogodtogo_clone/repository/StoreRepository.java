package com.fhtechnikum.toogodtogo_clone.repository;

import com.fhtechnikum.toogodtogo_clone.entity.StoreEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends ListCrudRepository<StoreEntity, UUID> {

    // Derived Query: SELECT ... WHERE city = ?  (fuer GET /stores?city=Wien)
    List<StoreEntity> findByCity(String city);
}
