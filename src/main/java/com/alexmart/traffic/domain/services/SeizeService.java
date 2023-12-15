package com.alexmart.traffic.domain.services;

import com.alexmart.traffic.domain.model.Vehicle;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SeizeService {

    private VehicleService vehicleService;

    @Transactional
    public Vehicle seize(Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        vehicle.seize();
        return vehicle;
    }

    @Transactional
    public Vehicle release(Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        vehicle.release();
        return vehicle;
    }
}
