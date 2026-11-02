package com.fhtechnikum.toogodtogo_clone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Ein Angebot ("Ueberraschungssackerl") eines Stores.
 */
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

    // Preis, den der Kunde zahlt
    private BigDecimal price;

    // Urspruenglicher Warenwert (zum Anzeigen der Ersparnis)
    private BigDecimal originalValue;

    // Wie viele Sackerl noch verfuegbar sind
    private int quantityAvailable;

    private LocalDateTime pickupStart;

    private LocalDateTime pickupEnd;

    // Viele Angebote gehoeren zu einem Store (Fremdschluessel store_id)
    @ManyToOne
    private StoreEntity store;

    // Wird ein Angebot geloescht, werden seine Reservierungen mitgeloescht.
    @OneToMany(mappedBy = "offer", cascade = CascadeType.REMOVE)
    private List<ReservationEntity> reservations = new ArrayList<>();

    public OfferEntity(String title, String description, BigDecimal price, BigDecimal originalValue,
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
