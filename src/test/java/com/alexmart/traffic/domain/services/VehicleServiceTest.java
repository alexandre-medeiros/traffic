package com.alexmart.traffic.domain.services;

import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.model.StatusVehicle;
import com.alexmart.traffic.domain.model.Vehicle;
import com.alexmart.traffic.domain.repository.OwnerRepository;
import com.alexmart.traffic.domain.repository.VehicleRepository;
import com.alexmart.traffic.modeltest.OwnerMotherTest;
import com.alexmart.traffic.modeltest.VehicleMotherTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VehicleServiceTest {

    private final Long vehicleId = 1L;
    private final Long ownerId = 1L;
    private final String plate = "SSS9999";

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private OwnerRepository ownerRepository;

    private VehicleService vehicleService;
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ownerService = new OwnerService(ownerRepository);
        vehicleService = new VehicleService(vehicleRepository, ownerService);
    }

    @Test
    void Given_a_list_of_vehicles_When_findAll_Then_return_the_list() {
        // Arrange
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        Vehicle vehicle1 = new Vehicle(owner, "Make1", "Model1", "ABC123", StatusVehicle.REGULAR,
                OffsetDateTime.now(), null);
        Vehicle vehicle2 = new Vehicle(owner, "Make2", "Model2", "DEF456", StatusVehicle.REGULAR,
                OffsetDateTime.now(), null);
        Vehicle vehicle3 = new Vehicle(owner, "Make3", "Model3", "GHI789", StatusVehicle.REGULAR,
                OffsetDateTime.now(), null);

        List<Vehicle> vehicles = Arrays.asList(vehicle1, vehicle2, vehicle3);
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleService.findAll();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(vehicle1));
        assertTrue(result.contains(vehicle2));
        assertTrue(result.contains(vehicle3));
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void Given_a_vehicle_id_When_findById_Then_return_the_vehicle() {
        // Arrange
        Vehicle expectedVehicle = VehicleMotherTest.getExistingVehicleWithId(vehicleId);
        when(vehicleRepository.findByIdOrFail(vehicleId)).thenReturn(expectedVehicle);

        // Act
        Vehicle foundVehicle = vehicleService.findById(vehicleId);

        // Assert
        assertNotNull(foundVehicle);
        assertEquals(expectedVehicle, foundVehicle);
        verify(vehicleRepository, times(1)).findByIdOrFail(vehicleId);
    }

    @Test
    void Given_a_vehicle_When_save_Then_return_the_saved_vehicle() {
        // Arrange
        Vehicle vehicleToSave = VehicleMotherTest.getNewVehicleWithOwnerId(ownerId);
        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(any(Owner.class));
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.empty());
        when(vehicleRepository.save(vehicleToSave)).thenAnswer(call -> {
            Vehicle argumentVehicle = call.getArgument(0, Vehicle.class);
            argumentVehicle.setId(vehicleId);
            return argumentVehicle;
        });

        // Act
        Vehicle result = vehicleService.save(vehicleToSave);

        // Assert
        verify(vehicleRepository, times(1)).save(vehicleToSave);
        verify(vehicleRepository, times(1)).findByPlate(anyString());
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
    }

    @Test
    void Given_a_vehicle_with_nonexistent_owner_When_save_Then_throw_EntityNotFoundException() {
        // Arrange
        Vehicle vehicleToSave = VehicleMotherTest.getNewVehicleWithOwnerId(ownerId);

        // This approach is used to mock the point that owner existence verify is realized
        // We did not use a call to ownerService.findById because our proposal was to mock
        // the point when the call to database is realized
        // See: VehicleService ==> OwnerService ==> OwnerRepository ==> throw the exception
        ownerService = new OwnerService(ownerRepository);
        vehicleService = new VehicleService(vehicleRepository, ownerService);

        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.empty());
        when(ownerRepository.findByIdOrFail(ownerId)).thenThrow(EntityNotFoundException.class);

        // Act
        EntityNotFoundException e = catchThrowableOfType(() -> vehicleService.save(vehicleToSave), EntityNotFoundException.class);

        // Assert
        assertNotNull(e);
        verify(vehicleRepository, times(1)).findByPlate(anyString());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void Given_a_vehicle_with_existing_plate_When_try_to_save_Then_throw_BusinessException() {
        //Arrange
        Vehicle vehicleToSave = VehicleMotherTest.getNewVehicleWithPlate(plate);
        Vehicle existingVehicle = VehicleMotherTest.getExistingVehicleWithPlate(plate);
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.of(existingVehicle));
        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(any(Owner.class));

        //Act
        BusinessException e = catchThrowableOfType(() -> vehicleService.save(vehicleToSave), BusinessException.class);

        //Assert
        assertNotNull(e);
        verify(vehicleRepository, times(1)).findByPlate(plate);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void Given_a_vehicle_When_update_Then_return_the_updated_vehicle() {
        // Arrange
        Vehicle vehicleToUpdate = VehicleMotherTest.getExistingVehicleWithId(vehicleId);
        when(vehicleRepository.findByPlate(plate)).thenReturn(Optional.of(vehicleToUpdate));
        when(vehicleRepository.save(vehicleToUpdate)).thenReturn(vehicleToUpdate);

        // Act
        Vehicle updatedVehicle = vehicleService.update(vehicleToUpdate);

        // Assert
        assertNotNull(updatedVehicle);
        verify(vehicleRepository, times(1)).findByPlate(anyString());
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
        verify(vehicleRepository, times(1)).save(vehicleToUpdate);
    }

    @Test
    void Given_a_vehicle_id_When_delete_Then_delete_the_vehicle() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle vehicleToDelete = VehicleMotherTest.getExistingVehicleWithId(vehicleId);
        when(vehicleRepository.findByIdOrFail(vehicleId)).thenReturn(vehicleToDelete);

        // Act
        vehicleService.delete(vehicleId);

        // Assert
        verify(vehicleRepository, times(1)).findByIdOrFail(vehicleId);
        verify(vehicleRepository, times(1)).delete(vehicleToDelete);
    }

    @Test
    void Given_a_vehicle_id_When_delete_and_vehicle_not_found_Then_throw_EntityNotFoundException() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle vehicleToDelete = VehicleMotherTest.getExistingVehicleWithId(vehicleId);
        when(vehicleRepository.findByIdOrFail(vehicleId)).thenThrow(EntityNotFoundException.class);

        // Act
        EntityNotFoundException e = catchThrowableOfType(() -> vehicleService.delete(vehicleId), EntityNotFoundException.class);

        // Assert
        assertNotNull(e);
        verify(vehicleRepository, times(1)).findByIdOrFail(vehicleId);
        verify(vehicleRepository, never()).delete(vehicleToDelete);
    }
}
