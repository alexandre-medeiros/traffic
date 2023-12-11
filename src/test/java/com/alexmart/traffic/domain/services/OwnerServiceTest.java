package com.alexmart.traffic.domain.services;

import com.alexmart.traffic.api.converter.OwnerConverter;
import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.repository.OwnerRepository;
import com.alexmart.traffic.modeltest.OwnerBuilderTest;
import com.alexmart.traffic.modeltest.OwnerMotherTest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
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
        OwnerBuilderTest builder = new OwnerBuilderTest();
        List<Owner> owners = Arrays.asList(
                builder.withId(1L).withName("John").withEmail("john.doe@example.com").build(),
                builder.withId(2L).withName("Jane").withEmail("jane.doe@example.com").build(),
                builder.withId(3L).withName("Bob").withEmail("bob.smith@example.com").build()
        );
        when(ownerRepository.findAll()).thenReturn(owners);

        //Act
        List<Owner> result = ownerService.findAll();

        //Assert
        assertThat(result).hasSize(3);
        assertThat(result)
                .flatMap(Owner::getName)
                .containsExactly("John", "Jane", "Bob");
    }

    @Test
    void Given_owner_when_findById_then_return_owner() {
        // Arrange
        long ownerId = 1L;
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(owner);

        // Act
        Owner result = ownerService.findById(ownerId);

        // Assert
        assertThat(result).isEqualTo(owner);
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
    }

    @Test
    void Given_owner_when_save_then_return_owner() {
        // Arrange
        Owner owner = OwnerMotherTest.getNewOwner();
        when(ownerRepository.findByEmail(owner.getEmail())).thenReturn(Optional.empty());
        when(ownerRepository.save(owner)).thenAnswer(call -> {
            Owner argumentOwner = call.getArgument(0, Owner.class);
            argumentOwner.setId(1L);
            return argumentOwner;
        });

        // Act
        Owner result = ownerService.save(owner);

        // Assert
        assertThat(result).isEqualTo(owner);
        verify(ownerRepository, times(1)).findByEmail(owner.getEmail());
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    void Given_existing_owner_when_update_with_new_values_then_return_updated_owner() {
        // Arrange
        long ownerId = 1L;
        Owner updatedOwner = OwnerMotherTest.getUpdatedOwnerWithId(ownerId);

        when(ownerRepository.save(updatedOwner)).thenAnswer(arg -> arg.getArgument(0, Owner.class));

        // Act
        Owner result = ownerService.update(updatedOwner);

        // Assert
        assertThat(result).isEqualTo(updatedOwner);
        verify(ownerRepository, times(1)).save(updatedOwner);
    }

    @Test
    void Given_owner_when_delete_then_owner_is_deleted() {
        // Arrange
        long ownerId = 1L;
        Owner owner = OwnerMotherTest.getOwnerWithId(ownerId);
        when(ownerRepository.findByIdOrFail(ownerId)).thenReturn(owner);

        // Act
        ownerService.delete(ownerId);

        // Assert
        verify(ownerRepository, times(1)).findByIdOrFail(ownerId);
        verify(ownerRepository, times(1)).delete(owner);
    }

    @Test
    void Given_owner_with_existing_email_when_save_then_throw_business_exception_and_not_save_owner() {
        // Arrange
        Owner existingOwner = OwnerMotherTest.getExistingOwnerWithEmail(email);
        Owner newOwner = OwnerMotherTest.getNewOwnerWithEmail(email);
        when(ownerRepository.findByEmail(eq(email))).thenReturn(Optional.of(existingOwner));

        //Act
        BusinessException e = catchThrowableOfType(() -> ownerService.save(newOwner), BusinessException.class);

        //Assert
        verify(ownerRepository, times(1)).findByEmail(eq(email));
        verify(ownerRepository, never()).save(any(Owner.class));
        assertThat(e)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Owner with the same email already exists");
    }
}