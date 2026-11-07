package com.fhtechnikum.toogodtogo_clone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

// a user reserves a number of bags of an offer
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

    // save the enum as text (RESERVED) instead of a number (0), easier to read in the db
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
