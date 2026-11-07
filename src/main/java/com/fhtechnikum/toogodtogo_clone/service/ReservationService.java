package com.fhtechnikum.toogodtogo_clone.service;

import com.fhtechnikum.toogodtogo_clone.dto.ReservationCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.ReservationDto;
import com.fhtechnikum.toogodtogo_clone.dto.ReservationUpdateDto;
import com.fhtechnikum.toogodtogo_clone.entity.OfferEntity;
import com.fhtechnikum.toogodtogo_clone.entity.ReservationEntity;
import com.fhtechnikum.toogodtogo_clone.entity.ReservationStatus;
import com.fhtechnikum.toogodtogo_clone.entity.UserEntity;
import com.fhtechnikum.toogodtogo_clone.repository.OfferRepository;
import com.fhtechnikum.toogodtogo_clone.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OfferRepository offerRepository;
    private final UserService userService;
    private final OfferService offerService;

    public List<ReservationDto> findAll() {
        List<ReservationDto> result = new ArrayList<>();
        for (ReservationEntity entity : reservationRepository.findAll()) {
            result.add(toDto(entity));
        }
        return result;
    }

    public List<ReservationDto> findByUser(UUID userId) {
        UserEntity user = userService.getUserEntity(userId); // 404 if the user does not exist

        List<ReservationDto> result = new ArrayList<>();
        for (ReservationEntity entity : reservationRepository.findByUserId(user.getId())) {
            result.add(toDto(entity));
        }
        return result;
    }

    public ReservationDto findById(UUID id) {
        ReservationEntity reservation = getReservationEntity(id);
        return toDto(reservation);
    }

    public ReservationDto create(ReservationCreationDto dto) {
        UserEntity user = userService.getUserEntity(dto.userId());
        OfferEntity offer = offerService.getOfferEntity(dto.offerId());

        // business rule: you can not reserve more bags than are available
        if (offer.getQuantityAvailable() < dto.quantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough bags available");
        }
        offer.setQuantityAvailable(offer.getQuantityAvailable() - dto.quantity());
        offerRepository.save(offer);

        ReservationEntity reservation = new ReservationEntity(user, offer, dto.quantity());
        ReservationEntity saved = reservationRepository.save(reservation);
        log.info("Reservation {} created for offer {} by user {}", saved.getId(), offer.getId(), user.getId());
        return toDto(saved);
    }

    public ReservationDto updateStatus(UUID id, ReservationUpdateDto dto) {
        ReservationEntity reservation = getReservationEntity(id);

        // only open reservations can be changed
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Reservation is already " + reservation.getStatus());
        }
        if (dto.status() == ReservationStatus.CANCELLED) {
            returnBagsToOffer(reservation);
        }
        reservation.setStatus(dto.status());
        ReservationEntity saved = reservationRepository.save(reservation);
        return toDto(saved);
    }

    public void delete(UUID id) {
        ReservationEntity reservation = getReservationEntity(id);
        if (reservation.getStatus() == ReservationStatus.RESERVED) {
            returnBagsToOffer(reservation);
        }
        reservationRepository.delete(reservation);
    }

    private void returnBagsToOffer(ReservationEntity reservation) {
        OfferEntity offer = reservation.getOffer();
        offer.setQuantityAvailable(offer.getQuantityAvailable() + reservation.getQuantity());
        offerRepository.save(offer);
    }

    // not in course material: ResponseStatusException
    // without it spring would return 500 (server error) instead of 404 (not found) for an unknown id
    private ReservationEntity getReservationEntity(UUID id) {
        Optional<ReservationEntity> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        return reservation.get();
    }

    private ReservationDto toDto(ReservationEntity entity) {
        return new ReservationDto(entity.getId(), entity.getUser().getId(), entity.getOffer().getId(),
                entity.getQuantity(), entity.getCreatedAt(), entity.getStatus());
    }
}
