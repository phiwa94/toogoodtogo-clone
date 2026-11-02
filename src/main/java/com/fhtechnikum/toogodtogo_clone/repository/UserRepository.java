package com.fhtechnikum.toogodtogo_clone.repository;

import com.fhtechnikum.toogodtogo_clone.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ListCrudRepository<UserEntity, UUID> {

    // Derived Query: SELECT ... WHERE email = ?
    boolean existsByEmail(String email);
}
