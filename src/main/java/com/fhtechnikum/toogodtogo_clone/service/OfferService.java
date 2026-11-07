package com.fhtechnikum.toogodtogo_clone.service;

import com.fhtechnikum.toogodtogo_clone.dto.OfferCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.OfferDto;
import com.fhtechnikum.toogodtogo_clone.entity.OfferEntity;
import com.fhtechnikum.toogodtogo_clone.entity.StoreEntity;
import com.fhtechnikum.toogodtogo_clone.repository.OfferRepository;
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
public class OfferService {

    private final OfferRepository offerRepository;
    private final StoreService storeService;

    public List<OfferDto> findAll() {
        List<OfferDto> result = new ArrayList<>();
        for (OfferEntity entity : offerRepository.findAll()) {
            result.add(toDto(entity));
        }
        return result;
    }

    public List<OfferDto> findByStore(UUID storeId) {
        StoreEntity store = storeService.getStoreEntity(storeId); // 404 if the store does not exist

        List<OfferDto> result = new ArrayList<>();
        for (OfferEntity entity : offerRepository.findByStoreId(store.getId())) {
            result.add(toDto(entity));
        }
        return result;
    }

    public OfferDto findById(UUID id) {
        OfferEntity offer = getOfferEntity(id);
        return toDto(offer);
    }

    public OfferDto create(OfferCreationDto dto) {
        validatePickupWindow(dto);
        StoreEntity store = storeService.getStoreEntity(dto.storeId());
        OfferEntity offer = new OfferEntity(dto.title(), dto.description(), dto.price(), dto.originalValue(),
                dto.quantityAvailable(), dto.pickupStart(), dto.pickupEnd(), store);
        OfferEntity saved = offerRepository.save(offer);
        return toDto(saved);
    }

    public OfferDto update(UUID id, OfferCreationDto dto) {
        validatePickupWindow(dto);
        OfferEntity offer = getOfferEntity(id);
        StoreEntity store = storeService.getStoreEntity(dto.storeId());
        offer.setTitle(dto.title());
        offer.setDescription(dto.description());
        offer.setPrice(dto.price());
        offer.setOriginalValue(dto.originalValue());
        offer.setQuantityAvailable(dto.quantityAvailable());
        offer.setPickupStart(dto.pickupStart());
        offer.setPickupEnd(dto.pickupEnd());
        offer.setStore(store);
        OfferEntity saved = offerRepository.save(offer);
        return toDto(saved);
    }

    public void delete(UUID id) {
        OfferEntity offer = getOfferEntity(id);
        offerRepository.delete(offer);
    }

    // also used by ReservationService
    //ResponseStatusException
    // without it spring would return 500 (server error) instead of 404 (not found) for an unknown id
    public OfferEntity getOfferEntity(UUID id) {
        Optional<OfferEntity> offer = offerRepository.findById(id);
        if (offer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found");
        }
        return offer.get();
    }

    // rules that use more than one field can not be checked with a single annotation
    private void validatePickupWindow(OfferCreationDto dto) {
        if (!dto.pickupEnd().isAfter(dto.pickupStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pickupEnd must be after pickupStart");
        }
        if (dto.price() > dto.originalValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must not be higher than originalValue");
        }
    }

    private OfferDto toDto(OfferEntity entity) {
        return new OfferDto(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getPrice(),
                entity.getOriginalValue(), entity.getQuantityAvailable(), entity.getPickupStart(),
                entity.getPickupEnd(), entity.getStore().getId());
    }
}
