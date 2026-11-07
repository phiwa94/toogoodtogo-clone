package com.fhtechnikum.toogodtogo_clone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// an offer (surprise bag) of a store
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    // price the customer pays
    private double price;

    // original value of the food (to show the savings)
    private double originalValue;

    // how many bags are still available
    private int quantityAvailable;

    private LocalDateTime pickupStart;

    private LocalDateTime pickupEnd;

    // many offers belong to one store (foreign key store_id)
    @ManyToOne
    private StoreEntity store;

    // when an offer is deleted, its reservations are deleted too
    // not in course material: mappedBy tells hibernate that the foreign key is on the @ManyToOne side
    // without it hibernate would create an extra join table
    @OneToMany(mappedBy = "offer", cascade = CascadeType.REMOVE)
    private List<ReservationEntity> reservations = new ArrayList<>();

    public OfferEntity(String title, String description, double price, double originalValue,
                       int quantityAvailable, LocalDateTime pickupStart, LocalDateTime pickupEnd,
                       StoreEntity store) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.originalValue = originalValue;
        this.quantityAvailable = quantityAvailable;
        this.pickupStart = pickupStart;
        this.pickupEnd = pickupEnd;
        this.store = store;
    }
}
