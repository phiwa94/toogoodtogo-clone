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

import java.util.List;
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
        return reservationRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<ReservationDto> findByUser(UUID userId) {
        userService.getUserEntity(userId); // 404, falls User nicht existiert
        return reservationRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public ReservationDto findById(UUID id) {
        return toDto(getReservationEntity(id));
    }

    public ReservationDto create(ReservationCreationDto dto) {
        UserEntity user = userService.getUserEntity(dto.userId());
        OfferEntity offer = offerService.getOfferEntity(dto.offerId());

        // Business-Regel: es koennen nicht mehr Sackerl reserviert werden als verfuegbar sind
        if (offer.getQuantityAvailable() < dto.quantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough bags available");
        }
        offer.setQuantityAvailable(offer.getQuantityAvailable() - dto.quantity());
        offerRepository.save(offer);

        ReservationEntity saved = reservationRepository.save(new ReservationEntity(user, offer, dto.quantity()));
        log.info("Reservation {} created for offer {} by user {}", saved.getId(), offer.getId(), user.getId());
        return toDto(saved);
    }

    public ReservationDto updateStatus(UUID id, ReservationUpdateDto dto) {
        ReservationEntity reservation = getReservationEntity(id);

        // Nur offene Reservierungen duerfen geaendert werden
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Reservation is already " + reservation.getStatus());
        }
        if (dto.status() == ReservationStatus.CANCELLED) {
            returnBagsToOffer(reservation);
        }
        reservation.setStatus(dto.status());
        return toDto(reservationRepository.save(reservation));
    }

    public void delete(UUID id) {
        ReservationEntity reservation = getReservationEntity(id);
        if (reservation.getStatus() == ReservationStatus.RESERVED) {
            returnBagsToOffer(reservation);
        }
        reservationRepository.deleteById(id);
    }

    private void returnBagsToOffer(ReservationEntity reservation) {
        OfferEntity offer = reservation.getOffer();
        offer.setQuantityAvailable(offer.getQuantityAvailable() + reservation.getQuantity());
        offerRepository.save(offer);
    }

    private ReservationEntity getReservationEntity(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    private ReservationDto toDto(ReservationEntity entity) {
        return new ReservationDto(entity.getId(), entity.getUser().getId(), entity.getOffer().getId(),
                entity.getQuantity(), entity.getCreatedAt(), entity.getStatus());
    }
}
