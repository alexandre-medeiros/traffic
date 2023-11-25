package com.alexmart.traffic.api.dto.vehicle;

import com.alexmart.traffic.api.dto.owner.OwnerIdDto;
import com.alexmart.traffic.domain.model.StatusVehicle;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
@Getter
@Setter
public class VehicleOutputDto {

    private OwnerIdDto owner;
    private String make;
    private String model;
    private String plate;
    private StatusVehicle status;
    private OffsetDateTime arrestedDate;
    private OffsetDateTime createdDate;
}