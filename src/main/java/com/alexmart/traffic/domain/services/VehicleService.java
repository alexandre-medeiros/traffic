package com.alexmart.traffic.domain.services;

import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.model.StatusVehicle;
import com.alexmart.traffic.domain.model.Vehicle;
import com.alexmart.traffic.domain.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;
    private OwnerService ownerService;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findByIdOrFail(id);
    }

    @Transactional
    public Vehicle save(Vehicle vehicle) {
        existOtherVehicleWithSamePlate(vehicle);
        ownerExists(vehicle);
        vehicle.setStatus(StatusVehicle.REGULAR);
        return vehicleRepository.save(vehicle);
    }

    private void ownerExists(Vehicle vehicle) {
        Owner owner = ownerService.findById(vehicle.getOwnerId());
        vehicle.setOwner(owner);
    }

    private void existOtherVehicleWithSamePlate(Vehicle vehicle) {
        boolean plateInUse = vehicleRepository
                .findByPlate(vehicle.getPlate())
                .filter(v -> !v.equals(vehicle))
                .isPresent();

        if (plateInUse) {
            throw new BusinessException("Plate already in use");
        }
    }

    @Transactional
    public Vehicle update(Vehicle vehicle) {
        return save(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = findById(id);
        vehicleRepository.delete(vehicle);
    }
}
