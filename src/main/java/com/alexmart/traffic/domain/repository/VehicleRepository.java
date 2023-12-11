package com.alexmart.traffic.domain.repository;

import com.alexmart.traffic.domain.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VehicleRepository extends CustomJpaRepository<Vehicle, Long>, JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByPlate(String plate);
}
