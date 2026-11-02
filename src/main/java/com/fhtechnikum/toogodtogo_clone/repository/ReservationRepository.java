package com.fhtechnikum.toogodtogo_clone.repository;

import com.fhtechnikum.toogodtogo_clone.entity.ReservationEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends ListCrudRepository<ReservationEntity, UUID> {
    List<ReservationEntity> findByUserId(UUID userId);
}
