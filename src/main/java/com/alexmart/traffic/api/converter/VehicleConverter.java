package com.alexmart.traffic.api.converter;

import com.alexmart.traffic.api.dto.vehicle.VehicleInputDto;
import com.alexmart.traffic.api.dto.vehicle.VehicleOutputDto;
import com.alexmart.traffic.domain.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleConverter {

    VehicleOutputDto vehicleToVehicleDto(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "arrestedDate", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "citations", ignore = true)
    Vehicle vehicleDtoToVehicle(VehicleInputDto vehicleDto);

    List<VehicleOutputDto> vehicleListToVehicleDtoList(List<Vehicle> vehicleList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "arrestedDate", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Vehicle updateVehicle(Vehicle updatedVehicle, @MappingTarget Vehicle existingVehicle);
}
