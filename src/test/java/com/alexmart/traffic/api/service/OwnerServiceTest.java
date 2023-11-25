package com.alexmart.traffic.api.service;

import com.alexmart.traffic.api.converter.OwnerConverter;
import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.repository.OwnerRepository;
import com.alexmart.traffic.domain.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OwnerServiceTest {

    private final String email = "existing@example.com";
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private OwnerConverter ownerConverter;
    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Given_owner_list_when_find_all_owners_then_return_owner_list() {
        // Arrange
        List<Owner> owners = Arrays.asList(
                new Owner(1L, "John", "Doe", "john.doe@example.com"),
                new Owner(2L, "Jane", "Doe", "jane.doe@example.com"),
                new Owner(3L, "Bob", "Smith", "bob.smith@example.com")
        );
        when(ownerRepository.findAll()).thenReturn(owners);

        //Act
        List<Owner> result = ownerService.findAll();

        //Assert
        assertEquals(3, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
        assertEquals("Bob", result.get(2).getName());
    }

    @Test
    void Given_owner_when_findById_then_return_owner() {
        // Arrange
        long ownerId = 1L;
        Owner owner = new Owner(ownerId, "John Doe", "john@example.com", "1234567890");
        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(owner);

        // Act
        Owner result = ownerService.findById(ownerId);

        // Assert
        assertEquals(owner, result);
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
    }

    @Test
    void Given_owner_when_save_then_return_owner() {
        // Arrange
        Owner owner = new Owner(null, "John Doe", "john@example.com", "1234567890");
        when(ownerRepository.findByEmail(owner.getEmail())).thenReturn(Optional.empty());
        when(ownerRepository.save(owner)).thenAnswer(call -> {
            Owner argumentOwner = call.getArgument(0, Owner.class);
            argumentOwner.setId(1L);
            return argumentOwner;
        });

        // Act
        Owner result = ownerService.save(owner);

        // Assert
        assertEquals(owner, result);
        verify(ownerRepository, times(1)).findByEmail(owner.getEmail());
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    void Given_existing_owner_when_update_with_new_values_then_return_updated_owner() {
        // Arrange
        long ownerId = 1L;
        Owner existingOwner = new Owner(ownerId, "John Doe", "john@example.com", "1234567890");
        Owner updatedOwner = new Owner(ownerId, "John Updated", "john@example.com", "9876543210");

        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(existingOwner);
        when(ownerRepository.save(updatedOwner)).thenReturn(updatedOwner);
        when(ownerConverter.updateOwner(updatedOwner, existingOwner))
                .thenAnswer(arg -> {
                    Owner updated = arg.getArgument(0, Owner.class);
                    Owner existing = arg.getArgument(1, Owner.class);
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    existing.setPhone(updated.getPhone());
                    return existing;
                });

        // Act
        Owner result = ownerService.update(ownerId, updatedOwner);

        // Assert
        assertEquals(updatedOwner, result);
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
        verify(ownerConverter, times(1)).updateOwner(updatedOwner, existingOwner);
        verify(ownerRepository, times(1)).save(updatedOwner);
    }

    @Test
    void Given_owner_when_delete_then_owner_is_deleted() {
        // Arrange
        long ownerId = 1L;
        Owner owner = new Owner(ownerId, "John Doe", "john@example.com", "1234567890");
        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(owner);

        // Act
        ownerService.delete(ownerId);

        // Assert
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
        verify(ownerRepository, times(1)).delete(owner);
    }

    @Test
    void Given_owner_with_existing_email_when_save_then_throw_exception() {
        // Arrange
        Owner existingOwner = new Owner();
        existingOwner.setId(1L);
        existingOwner.setEmail(email);

        // Mock behavior for findByEmail
        when(ownerRepository.findByEmail(eq(email))).thenReturn(Optional.of(existingOwner));

        // Create a new owner with the same email
        Owner newOwner = new Owner();
        newOwner.setEmail(email);

        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ownerService.save(newOwner);
        });

        assertEquals("Owner with the same email already exists", exception.getMessage());
        verify(ownerRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    void Given_save_owner_with_existing_email_when_try_to_save_then_throw_exception_and_not_save_owner() {
        // Arrange

        // Mock behavior for findByEmail
        Owner existingOwner = new Owner();
        existingOwner.setId(1L);
        existingOwner.setEmail(email);
        when(ownerRepository.findByEmail(eq(email))).thenReturn(Optional.of(existingOwner));

        // Create a new owner with the same email
        Owner newOwner = new Owner();
        newOwner.setEmail(email);

        // Act and Assert
        assertAll("If throw exception then not save owner",
                () -> assertThrows(BusinessException.class, () -> ownerService.save(newOwner)),
                () -> verify(ownerRepository, times(1)).findByEmail(eq(email)),
                () -> verify(ownerRepository, never()).save(newOwner)
        );
    }
}