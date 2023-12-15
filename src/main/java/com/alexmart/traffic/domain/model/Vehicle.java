package com.alexmart.traffic.domain.model;

import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Citation> citations = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private StatusVehicle status;

    private String make;
    private String model;
    private String plate;

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

    public Citation addCitation(Citation citation) {
        citation.setVehicle(this);
        citations.add(citation);
        return citation;
    }

    public Citation findCitation(Long id) {
        return citations
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Citation %d to vehicle %d not found", id, this.id)));
    }

    public void seize() {
        if (isAlreadyArrested()) {
            throw new BusinessException(String.format("Vehicle with %d is already arrested.", id));
        }

        this.status = StatusVehicle.ARRESTED;
        this.arrestedDate = OffsetDateTime.now();
    }

    public void release() {
        if (!isAlreadyArrested()) {
            throw new BusinessException(String.format("Vehicle with %d is not arrested.", id));
        }

        this.status = StatusVehicle.REGULAR;
        this.arrestedDate = null;
    }

    private boolean isAlreadyArrested() {
        return this.status.equals(StatusVehicle.ARRESTED);
    }
}