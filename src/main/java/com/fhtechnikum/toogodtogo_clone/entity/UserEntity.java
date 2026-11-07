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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    // a user has many reservations, when the user is deleted their reservations are deleted too
    // not in course material: mappedBy tells hibernate that the foreign key is on the @ManyToOne side
    // without it hibernate would create an extra join table
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ReservationEntity> reservations = new ArrayList<>();

    public UserEntity(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
