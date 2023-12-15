package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.converter.VehicleConverter;
import com.alexmart.traffic.api.dto.vehicle.VehicleOutputDto;
import com.alexmart.traffic.domain.services.SeizeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/vehicles/{vehicleId}/seize")
public class SeizeController {

    private SeizeService seizeService;
    private VehicleConverter vehicleConverter;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VehicleOutputDto seize(@PathVariable Long vehicleId) {
        return vehicleConverter.vehicleToVehicleDto(seizeService.seize(vehicleId));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public VehicleOutputDto release(@PathVariable Long vehicleId) {
        return vehicleConverter.vehicleToVehicleDto(seizeService.release(vehicleId));
    }
}
