package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.converter.VehicleConverter;
import com.alexmart.traffic.api.dto.vehicle.VehicleInputDto;
import com.alexmart.traffic.api.dto.vehicle.VehicleOutputDto;
import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.model.StatusVehicle;
import com.alexmart.traffic.domain.model.Vehicle;
import com.alexmart.traffic.domain.repository.OwnerRepository;
import com.alexmart.traffic.domain.repository.VehicleRepository;
import com.alexmart.traffic.domain.services.OwnerService;
import com.alexmart.traffic.domain.services.VehicleService;
import com.alexmart.traffic.modeltest.OwnerMotherTest;
import com.alexmart.traffic.modeltest.VehicleMotherTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VehicleControllerTest {

    private final Long vehicleId = 1L;
    private final Long ownerId = 1L;
    private final Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Spy
    private VehicleConverter vehicleConverter = Mappers.getMapper(VehicleConverter.class);

    private VehicleController vehicleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        OwnerService ownerService = new OwnerService(ownerRepository);
        VehicleService vehicleService = new VehicleService(vehicleRepository, ownerService);
        vehicleController = new VehicleController(vehicleService, vehicleConverter);
    }

    @Test
    void Given_a_list_of_vehicles_When_findAll_Then_return_the_list() {
        // Arrange
        List<Vehicle> mockVehicles = VehicleMotherTest.createMockVehicleList();
        List<VehicleOutputDto> mockDtoList = VehicleMotherTest.createMockDtoList(mockVehicles);

        when(vehicleRepository.findAll()).thenReturn(mockVehicles);

        // Act
        List<VehicleOutputDto> result = vehicleController.findAll();

        // Assert
        assertEquals(mockDtoList, result);
        verify(vehicleConverter, times(1)).vehicleListToVehicleDtoList(mockVehicles);
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void Given_a_vehicle_when_findById_then_return_the_vehicle() {
        //Arrange
        Vehicle existingVehicle = VehicleMotherTest.getExistingVehicle();
        VehicleOutputDto existingDtoVehicle = VehicleMotherTest.mapToDto(existingVehicle);
        when(vehicleRepository.findByIdOrFail(vehicleId)).thenReturn(existingVehicle);

        //Act
        VehicleOutputDto vehicleDto = vehicleController.find(vehicleId);

        //Assert
        assertEquals(existingDtoVehicle, vehicleDto);
        verify(vehicleRepository, times(1)).findByIdOrFail(vehicleId);
        verify(vehicleConverter, times(1)).vehicleToVehicleDto(existingVehicle);
    }

    @Test
    void Given_a_vehicle_when_create_then_return_the_vehicle() {
        //Arrange
        OffsetDateTime now = OffsetDateTime.now();
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        VehicleInputDto vehicleInputDto = VehicleMotherTest.getNewVehicleInputDto();
        VehicleOutputDto vehicleOutputDto = VehicleMotherTest.getNewVehicleOutputDto(vehicleInputDto, owner, now);
        Vehicle entity = VehicleMotherTest.mapToEntity(vehicleInputDto);

        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(owner);
        when(vehicleRepository.findByPlate(entity.getPlate())).thenReturn(Optional.empty());
        when(vehicleRepository.save(entity)).thenAnswer(invocation -> {
            Vehicle vehicleToSave = invocation.getArgument(0, Vehicle.class);
            vehicleToSave.setId(vehicleId);
            vehicleToSave.setOwner(owner);
            vehicleToSave.setCreatedDate(now);
            vehicleToSave.setStatus(StatusVehicle.REGULAR);

            return vehicleToSave;
        });

        //Act
        VehicleOutputDto result = vehicleController.create(vehicleInputDto);
        Vehicle savedEntity = vehicleRepository.save(entity);

        //Assert
        assertEquals(vehicleOutputDto, result);
        verify(vehicleConverter, times(1)).vehicleToVehicleDto(savedEntity);
        verify(vehicleRepository, times(2)).save(entity);
    }

    @Test
    void Given_a_existing_vehicle_when_update_then_return_the_updated_vehicle() {
        //Arrange
        VehicleInputDto vehicleInputDto = VehicleMotherTest.getUpdatedVehicleInputDto();
        Vehicle existingVehicle = getExistingVehicle();
        VehicleOutputDto vehicleOutputDto = getVehicleOutputDtoUpdated(existingVehicle, vehicleInputDto);
        Vehicle vehicleToUpdate = getVehicleToUpdate(existingVehicle, vehicleInputDto);

        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(owner);
        when(vehicleRepository.findByIdOrFail(vehicleId)).thenReturn(existingVehicle);
        when(vehicleRepository.findByPlate(vehicleToUpdate.getPlate())).thenReturn(Optional.of(vehicleToUpdate));
        when(vehicleRepository.save(vehicleToUpdate)).thenReturn(vehicleToUpdate);

        //Act
        VehicleOutputDto result = vehicleController.update(vehicleId, vehicleInputDto);
        Vehicle savedEntity = vehicleRepository.save(vehicleToUpdate);

        //Assert
        assertEquals(vehicleOutputDto, result);
        verify(vehicleConverter, times(1)).vehicleToVehicleDto(savedEntity);
        verify(vehicleRepository, times(2)).save(vehicleToUpdate);
    }

    @Test
    void Given_a_inexistent_vehicle_when_try_to_update_then_throw_EntityNotFoundException() {
        //Arrange

        when(ownerRepository.findByIdOrFail(ownerId)).thenThrow(EntityNotFoundException.class);
        when(vehicleRepository.findByIdOrFail(vehicleId)).thenThrow(EntityNotFoundException.class);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(any(Vehicle.class));

        //Act
        EntityNotFoundException e = catchThrowableOfType(() -> vehicleController.update(vehicleId, new VehicleInputDto()), EntityNotFoundException.class);

        //Assert
        verify(vehicleRepository, never()).save(any(Vehicle.class));
        verify(vehicleRepository, times(1)).findByIdOrFail(vehicleId);
    }

    @Test
    void Given_a_vehicle_when_try_to_update_and_owner_does_not_exist_then_throw_EntityNotFoundException() {
        //Arrange
        VehicleInputDto vehicleInputDto = VehicleMotherTest.getUpdatedVehicleInputDto();
        Vehicle existingVehicle = getExistingVehicle();
        VehicleOutputDto vehicleOutputDto = getVehicleOutputDtoUpdated(existingVehicle, vehicleInputDto);
        Vehicle vehicleToUpdate = getVehicleToUpdate(existingVehicle, vehicleInputDto);

        when(vehicleRepository.findByIdOrFail(vehicleId)).thenReturn(existingVehicle);
        when(vehicleRepository.findByPlate(vehicleToUpdate.getPlate())).thenReturn(Optional.of(vehicleToUpdate));
        when(ownerRepository.findByIdOrFail(ownerId)).thenThrow(EntityNotFoundException.class);
        when(vehicleRepository.save(vehicleToUpdate)).thenReturn(vehicleToUpdate);

        //Act
        EntityNotFoundException e = catchThrowableOfType(() -> vehicleController.update(vehicleId, vehicleInputDto), EntityNotFoundException.class);

        //Assert
        assertNotNull(e);
        verify(vehicleRepository, times(1)).findByIdOrFail(vehicleId);
        verify(vehicleRepository, times(1)).findByPlate(vehicleToUpdate.getPlate());
        verify(vehicleRepository, never()).save(vehicleToUpdate);
    }

    private Vehicle getVehicleToUpdate(Vehicle existingVehicle, VehicleInputDto vehicleInputDto) {
        existingVehicle.setPlate(vehicleInputDto.getPlate());
        existingVehicle.setModel(vehicleInputDto.getModel());
        existingVehicle.setStatus(StatusVehicle.REGULAR);
        return existingVehicle;
    }

    private Vehicle getExistingVehicle() {
        Vehicle existingVehicle = VehicleMotherTest.getExistingVehicleWithId(vehicleId);
        existingVehicle.setOwner(owner);
        return existingVehicle;
    }

    private VehicleOutputDto getVehicleOutputDtoUpdated(Vehicle existingVehicle, VehicleInputDto vehicleInputDto) {
        VehicleOutputDto vehicleOutputDto = VehicleMotherTest.mapToDto(existingVehicle);
        vehicleOutputDto.setMake(vehicleInputDto.getMake());
        vehicleOutputDto.setModel(vehicleInputDto.getModel());
        vehicleOutputDto.setPlate(vehicleInputDto.getPlate());
        return vehicleOutputDto;
    }

}

