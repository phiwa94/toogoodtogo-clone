package com.fhtechnikum.toogodtogo_clone.service;

import com.fhtechnikum.toogodtogo_clone.dto.StoreCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.StoreDto;
import com.fhtechnikum.toogodtogo_clone.entity.StoreEntity;
import com.fhtechnikum.toogodtogo_clone.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // city is optional: null means all stores, otherwise filter by city
    public List<StoreDto> findAll(String city) {
        List<StoreEntity> stores;
        if (city == null) {
            stores = storeRepository.findAll();
        } else {
            stores = storeRepository.findByCity(city);
        }

        List<StoreDto> result = new ArrayList<>();
        for (StoreEntity entity : stores) {
            result.add(toDto(entity));
        }
        return result;
    }

    public StoreDto findById(UUID id) {
        StoreEntity store = getStoreEntity(id);
        return toDto(store);
    }

    public StoreDto create(StoreCreationDto dto) {
        StoreEntity store = new StoreEntity(dto.name(), dto.description(), dto.street(), dto.postalCode(), dto.city());
        StoreEntity saved = storeRepository.save(store);
        return toDto(saved);
    }

    public StoreDto update(UUID id, StoreCreationDto dto) {
        StoreEntity store = getStoreEntity(id);
        store.setName(dto.name());
        store.setDescription(dto.description());
        store.setStreet(dto.street());
        store.setPostalCode(dto.postalCode());
        store.setCity(dto.city());
        StoreEntity saved = storeRepository.save(store);
        return toDto(saved);
    }

    public void delete(UUID id) {
        StoreEntity store = getStoreEntity(id);
        storeRepository.delete(store);
    }

    // also used by OfferService
    //responseStatusException
    // without it spring would return 500 (server error) instead of 404 (not found) for an unknown id
    public StoreEntity getStoreEntity(UUID id) {
        Optional<StoreEntity> store = storeRepository.findById(id);
        if (store.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found");
        }
        return store.get();
    }

    private StoreDto toDto(StoreEntity entity) {
        return new StoreDto(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getStreet(), entity.getPostalCode(), entity.getCity());
    }
}
