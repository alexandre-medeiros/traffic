package com.alexmart.traffic.domain.services;

import com.alexmart.traffic.domain.model.Citation;
import com.alexmart.traffic.domain.model.Vehicle;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class CitationService {

    private VehicleService vehicleService;

    public List<Citation> findAll(Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        return vehicle.getCitations();
    }

    public Citation find(Long vehicleId, Long citationId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        return vehicle.findCitation(citationId);
    }

    @Transactional
    public Citation createCitation(Long vehicleId, Citation citation) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        return vehicle.addCitation(citation);
    }
}
