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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final StoreService storeService;

    public List<OfferDto> findAll() {
        return offerRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<OfferDto> findByStore(UUID storeId) {
        storeService.getStoreEntity(storeId); // 404, falls Store nicht existiert
        return offerRepository.findByStoreId(storeId).stream()
                .map(this::toDto)
                .toList();
    }

    public OfferDto findById(UUID id) {
        return toDto(getOfferEntity(id));
    }

    public OfferDto create(OfferCreationDto dto) {
        validatePickupWindow(dto);
        StoreEntity store = storeService.getStoreEntity(dto.storeId());
        OfferEntity offer = new OfferEntity(dto.title(), dto.description(), dto.price(), dto.originalValue(),
                dto.quantityAvailable(), dto.pickupStart(), dto.pickupEnd(), store);
        return toDto(offerRepository.save(offer));
    }

    public OfferDto update(UUID id, OfferCreationDto dto) {
        validatePickupWindow(dto);
        OfferEntity offer = getOfferEntity(id);
        offer.setTitle(dto.title());
        offer.setDescription(dto.description());
        offer.setPrice(dto.price());
        offer.setOriginalValue(dto.originalValue());
        offer.setQuantityAvailable(dto.quantityAvailable());
        offer.setPickupStart(dto.pickupStart());
        offer.setPickupEnd(dto.pickupEnd());
        offer.setStore(storeService.getStoreEntity(dto.storeId()));
        return toDto(offerRepository.save(offer));
    }

    public void delete(UUID id) {
        getOfferEntity(id); // wirft 404, falls nicht vorhanden
        offerRepository.deleteById(id);
    }

    // Wird auch vom ReservationService verwendet
    public OfferEntity getOfferEntity(UUID id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found"));
    }

    // Regeln, die mehrere Felder betreffen, koennen nicht mit einer einzelnen Annotation geprueft werden
    private void validatePickupWindow(OfferCreationDto dto) {
        if (!dto.pickupEnd().isAfter(dto.pickupStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pickupEnd must be after pickupStart");
        }
        if (dto.price().compareTo(dto.originalValue()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must not be higher than originalValue");
        }
    }

    private OfferDto toDto(OfferEntity entity) {
        return new OfferDto(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getPrice(),
                entity.getOriginalValue(), entity.getQuantityAvailable(), entity.getPickupStart(),
                entity.getPickupEnd(), entity.getStore().getId());
    }
}
