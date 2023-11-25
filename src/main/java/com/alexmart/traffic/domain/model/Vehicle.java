package com.alexmart.traffic.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.OffsetDateTime;

@Data
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
    @Column(name = "created_date")
    private OffsetDateTime createdDate;
    @Column(name = "arrested_date")
    private OffsetDateTime arrestedDate;
}