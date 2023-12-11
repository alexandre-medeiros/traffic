package com.alexmart.traffic.modeltest;

import com.alexmart.traffic.api.dto.owner.OwnerIdDto;
import com.alexmart.traffic.api.dto.owner.OwnerOutputDto;
import com.alexmart.traffic.api.dto.vehicle.VehicleInputDto;
import com.alexmart.traffic.api.dto.vehicle.VehicleOutputDto;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.model.StatusVehicle;
import com.alexmart.traffic.domain.model.Vehicle;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class VehicleMotherTest {

    private static final Long ownerId = 1L;
    private static final Long vehicleId = 1L;

    private VehicleMotherTest() {
    }

    public static Vehicle getNewVehicle() {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        return VehicleBuilderTest
                .builder()
                .withOwner(owner)
                .withMake("Make1")
                .withModel("Model1")
                .withPlate("ABC123")
                .withStatus(StatusVehicle.REGULAR)
                .withCreatedDate(OffsetDateTime.now())
                .withArrestedDate(null)
                .build().getVehicle();
    }

    public static VehicleInputDto getNewVehicleInputDto() {
        VehicleInputDto dto = new VehicleInputDto();
        OwnerIdDto ownerIdDto = new OwnerIdDto();
        ownerIdDto.setId(ownerId);
        dto.setOwner(ownerIdDto);
        dto.setMake("Make1");
        dto.setModel("Model1");
        dto.setPlate("ABC123");
        return dto;
    }

    public static VehicleInputDto getUpdatedVehicleInputDto() {
        VehicleInputDto dto = new VehicleInputDto();
        OwnerIdDto ownerIdDto = new OwnerIdDto();
        ownerIdDto.setId(ownerId);
        dto.setOwner(ownerIdDto);
        dto.setMake("Make111");
        dto.setModel("Model111");
        dto.setPlate("ABC9999");
        return dto;
    }

    public static VehicleOutputDto getNewVehicleOutputDto(Long id) {
        VehicleOutputDto dto = new VehicleOutputDto();
        dto.setId(id);
        return dto;
    }

    public static Vehicle getNewVehicleWithOwnerId(Long id) {
        Owner owner = OwnerMotherTest.getOwnerWithId(id);
        Vehicle vehicle = getNewVehicle();
        vehicle.setOwner(owner);
        return vehicle;
    }

    public static Vehicle getNewVehicleWithPlate(String plate) {
        Vehicle vehicle = getNewVehicle();
        vehicle.setPlate(plate);
        return vehicle;
    }

    public static Vehicle getNewVehicleWithId(Long id) {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle = getNewVehicle();
        vehicle.setId(id);
        vehicle.setOwner(owner);
        return vehicle;
    }

    public static Vehicle getExistingVehicle() {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle = getNewVehicle();
        vehicle.setId(vehicleId);
        vehicle.setOwner(owner);
        return vehicle;
    }

    public static VehicleOutputDto getExistingVehicleDto() {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle = getNewVehicle();
        vehicle.setId(vehicleId);
        vehicle.setOwner(owner);
        return mapToDto(vehicle);
    }

    public static Vehicle getExistingVehicleWithId(Long id) {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle = getExistingVehicle();
        vehicle.setId(id);
        return vehicle;
    }

    public static Vehicle getExistingVehicleWithPlate(String plate) {
        Vehicle vehicle = getExistingVehicle();
        vehicle.setPlate(plate);
        return vehicle;
    }

    public static Vehicle getExistingVehicleWithIdAndOwnerId(Long ownerId, Long id) {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle = getExistingVehicle();
        vehicle.setId(id);
        return vehicle;
    }

    public static VehicleOutputDto mapToDto(Vehicle vehicle) {
        VehicleOutputDto dto = new VehicleOutputDto();
        dto.setId(vehicle.getId());
        dto.setOwner(OwnerMotherTest.mapToDto(vehicle.getOwner()));
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setPlate(vehicle.getPlate());
        dto.setStatus(vehicle.getStatus());
        dto.setArrestedDate(vehicle.getArrestedDate());
        dto.setCreatedDate(vehicle.getCreatedDate());
        return dto;
    }

    public static Vehicle mapToEntity(VehicleInputDto vehicle) {
        Owner owner = new Owner();
        owner.setId(vehicle.getOwner().getId());

        Vehicle entity = new Vehicle();
        entity.setOwner(owner);
        entity.setMake(vehicle.getMake());
        entity.setModel(vehicle.getModel());
        entity.setPlate(vehicle.getPlate());
        return entity;
    }

    public static List<Vehicle> createMockVehicleList() {
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle1 = new Vehicle(owner, "Make1", "Model1", "ABC123", StatusVehicle.REGULAR,
                OffsetDateTime.now(), null);
        Vehicle vehicle2 = new Vehicle(owner, "Make2", "Model2", "DEF456", StatusVehicle.REGULAR,
                OffsetDateTime.now(), null);
        Vehicle vehicle3 = new Vehicle(owner, "Make3", "Model3", "GHI789", StatusVehicle.REGULAR,
                OffsetDateTime.now(), null);

        return Arrays.asList(vehicle1, vehicle2, vehicle3);
    }

    public static List<VehicleOutputDto> createMockDtoList(List<Vehicle> vehicles) {
        VehicleOutputDto vehicleDto1 = mapToDto(vehicles.get(0));
        VehicleOutputDto vehicleDto2 = mapToDto(vehicles.get(1));
        VehicleOutputDto vehicleDto3 = mapToDto(vehicles.get(2));

        return new ArrayList<>(Arrays.asList(vehicleDto1, vehicleDto2, vehicleDto3));
    }

    public static VehicleOutputDto getNewVehicleOutputDto(VehicleInputDto vehicleInputDto, Owner owner, OffsetDateTime now) {
        Vehicle entity = VehicleMotherTest.mapToEntity(vehicleInputDto);
        OwnerOutputDto ownerOutputDto = OwnerMotherTest.mapToDto(owner);
        entity.setOwner(owner);
        VehicleOutputDto vehicleOutputDto = VehicleMotherTest.mapToDto(entity);
        vehicleOutputDto.setStatus(StatusVehicle.REGULAR);
        vehicleOutputDto.setOwner(ownerOutputDto);
        vehicleOutputDto.setId(vehicleId);
        vehicleOutputDto.setCreatedDate(now);

        return vehicleOutputDto;
    }
}
