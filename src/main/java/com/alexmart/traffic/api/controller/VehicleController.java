package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.converter.VehicleConverter;
import com.alexmart.traffic.api.dto.vehicle.VehicleInputDto;
import com.alexmart.traffic.api.dto.vehicle.VehicleOutputDto;
import com.alexmart.traffic.domain.model.Vehicle;
import com.alexmart.traffic.domain.services.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleConverter vehicleConverter;

    @RequestMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleOutputDto> findAll() {
        List<Vehicle> vehicles = vehicleService.findAll();
        return vehicleConverter.vehicleListToVehicleDtoList(vehicles);
    }

    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleOutputDto find(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.findById(id);
        return vehicleConverter.vehicleToVehicleDto(vehicle);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleOutputDto create(@RequestBody @Valid VehicleInputDto vehicleDto) {
        Vehicle vehicle = vehicleConverter.vehicleDtoToVehicle(vehicleDto);
        return vehicleConverter.vehicleToVehicleDto(vehicleService.save(vehicle));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleOutputDto update(@PathVariable Long id, @RequestBody @Valid VehicleInputDto vehicleDto) {
        Vehicle existingVehicle = vehicleService.findById(id);
        Vehicle updatedVehicle = vehicleConverter.vehicleDtoToVehicle(vehicleDto);
        Vehicle vehicle = vehicleConverter.updateVehicle(updatedVehicle, existingVehicle);
        return vehicleConverter.vehicleToVehicleDto(vehicleService.update(vehicle));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehicleService.delete(id);
    }
}