package com.alexmart.traffic.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Vehicle {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Owner owner;
    private String make;
    private String model;
    private String plate;
    @Enumerated(EnumType.STRING)
    private StatusVehicle status;
    @CreationTimestamp
    private OffsetDateTime createdDate;
    private OffsetDateTime arrestedDate;

    public Vehicle(Owner owner, String make, String model, String plate, StatusVehicle status, OffsetDateTime createdDate, OffsetDateTime arrestedDate) {
        this.owner = owner;
        this.make = make;
        this.model = model;
        this.plate = plate;
        this.status = status;
        this.createdDate = createdDate;
        this.arrestedDate = arrestedDate;
    }

    public Long getOwnerId() {
        return owner.getId();
    }
}