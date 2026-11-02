package com.fhtechnikum.toogodtogo_clone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Ein User reserviert eine bestimmte Anzahl Sackerl eines Angebots.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private OfferEntity offer;

    private int quantity;

    private LocalDateTime createdAt;

    // Enum als Text speichern ("RESERVED") statt als Zahl
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public ReservationEntity(UserEntity user, OfferEntity offer, int quantity) {
        this.user = user;
        this.offer = offer;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.RESERVED;
    }
}
