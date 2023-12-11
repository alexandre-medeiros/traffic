package com.alexmart.traffic.modeltest;

import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.model.StatusVehicle;
import com.alexmart.traffic.domain.model.Vehicle;
import lombok.Builder;
import java.time.OffsetDateTime;

@Builder(setterPrefix = "with")
public class VehicleBuilderTest {

    private Long id;
    private Owner owner;
    private String make;
    private String model;
    private String plate;
    private StatusVehicle status;
    private OffsetDateTime createdDate;
    private OffsetDateTime arrestedDate;

    public Vehicle getVehicle() {
        return new Vehicle(owner, make, model, plate, status, createdDate, arrestedDate);
    }

}
