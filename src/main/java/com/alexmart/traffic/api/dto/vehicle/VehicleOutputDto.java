package com.alexmart.traffic.api.dto.vehicle;

import com.alexmart.traffic.api.dto.owner.OwnerOutputDto;
import com.alexmart.traffic.domain.model.StatusVehicle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
@Getter
@Setter
@EqualsAndHashCode
public class VehicleOutputDto {

    private Long id;
    private OwnerOutputDto owner;
    private String make;
    private String model;
    private String plate;
    private StatusVehicle status;
    private OffsetDateTime arrestedDate;
    private OffsetDateTime createdDate;
}