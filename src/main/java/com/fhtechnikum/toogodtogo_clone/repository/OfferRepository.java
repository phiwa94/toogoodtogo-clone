package com.fhtechnikum.toogodtogo_clone.repository;

import com.fhtechnikum.toogodtogo_clone.entity.OfferEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends ListCrudRepository<OfferEntity, UUID> {
    List<OfferEntity> findByStoreId(UUID storeId);
}
