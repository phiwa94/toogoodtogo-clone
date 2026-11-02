package com.fhtechnikum.toogodtogo_clone.service;

import com.fhtechnikum.toogodtogo_clone.dto.StoreCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.StoreDto;
import com.fhtechnikum.toogodtogo_clone.entity.StoreEntity;
import com.fhtechnikum.toogodtogo_clone.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // city ist optional: null -> alle Stores, sonst nach Stadt gefiltert
    public List<StoreDto> findAll(String city) {
        List<StoreEntity> stores = (city == null)
                ? storeRepository.findAll()
                : storeRepository.findByCity(city);
        return stores.stream()
                .map(this::toDto)
                .toList();
    }

    public StoreDto findById(UUID id) {
        return toDto(getStoreEntity(id));
    }

    public StoreDto create(StoreCreationDto dto) {
        StoreEntity store = new StoreEntity(dto.name(), dto.description(), dto.street(), dto.postalCode(), dto.city());
        return toDto(storeRepository.save(store));
    }

    public StoreDto update(UUID id, StoreCreationDto dto) {
        StoreEntity store = getStoreEntity(id);
        store.setName(dto.name());
        store.setDescription(dto.description());
        store.setStreet(dto.street());
        store.setPostalCode(dto.postalCode());
        store.setCity(dto.city());
        return toDto(storeRepository.save(store));
    }

    public void delete(UUID id) {
        getStoreEntity(id); // wirft 404, falls nicht vorhanden
        storeRepository.deleteById(id);
    }

    // Wird auch vom OfferService verwendet
    public StoreEntity getStoreEntity(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));
    }

    private StoreDto toDto(StoreEntity entity) {
        return new StoreDto(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getStreet(), entity.getPostalCode(), entity.getCity());
    }
}
