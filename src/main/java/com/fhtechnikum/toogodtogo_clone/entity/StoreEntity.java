package com.fhtechnikum.toogodtogo_clone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private String street;

    private String postalCode;

    private String city;

    // Ein Store hat viele Angebote. Wird der Store geloescht, werden seine Angebote mitgeloescht.
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<OfferEntity> offers = new ArrayList<>();

    public StoreEntity(String name, String description, String street, String postalCode, String city) {
        this.name = name;
        this.description = description;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }
}
