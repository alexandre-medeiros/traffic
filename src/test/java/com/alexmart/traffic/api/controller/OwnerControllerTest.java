package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.converter.OwnerConverter;
import com.alexmart.traffic.api.dto.owner.OwnerInputDto;
import com.alexmart.traffic.api.dto.owner.OwnerOutputDto;
import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.repository.OwnerRepository;
import com.alexmart.traffic.domain.services.OwnerService;
import com.alexmart.traffic.modeltest.OwnerMotherTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OwnerControllerTest {

    public static final long ID = 1L;
    @Mock
    private OwnerRepository ownerRepository;
    private OwnerService ownerService;
    private OwnerController ownerController;
    @Spy
    private OwnerConverter ownerConverter = Mappers.getMapper(OwnerConverter.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ownerService = new OwnerService(ownerRepository);
        ownerController = new OwnerController(ownerService, ownerConverter);
    }

    @Test
    void Given_a_Owner_id_When_find_Owner_Then_return_Owner() {
        //Arrange
        Owner owner = OwnerMotherTest.getExistingOwner();
        OwnerOutputDto ownerOutputDto = OwnerMotherTest.mapToDto(owner);
        when(ownerRepository.findByIdOrFail(ID)).thenReturn(owner);

        //Act
        OwnerOutputDto result = ownerController.find(ID);

        //Assert
        assert result.equals(ownerOutputDto);
        verify(ownerRepository, times(1)).findByIdOrFail(ID);
    }

    @Test
    void Given_a_Owner_id_of_a_inexistent_Owner_When_find_Then_Throw_EntityNotFoundException() {
        //Arrange
        when(ownerRepository.findByIdOrFail(ID)).thenThrow(EntityNotFoundException.class);

        //Act
        EntityNotFoundException e = catchThrowableOfType(() -> ownerController.find(ID), EntityNotFoundException.class);

        //Assert
        assertNotNull(e);
        verify(ownerRepository, times(1)).findByIdOrFail(ID);
    }

    @Test
    void Given_a_List_owner_When_findAll_Then_return_List_owner() {
        //Arrange
        List<Owner> ownerList = OwnerMotherTest.getExistingOwnerList();
        List<OwnerOutputDto> ownerOutputDtoList = OwnerMotherTest.mapToDtoList(ownerList);
        when(ownerRepository.findAll()).thenReturn(ownerList);

        //Act
        List<OwnerOutputDto> result = ownerController.findAll();

        //Assert
        assert result.equals(ownerOutputDtoList);
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void Given_Valid_OwnerInputDto_When_Create_Then_Return_Created_OwnerOutputDto() {
        // Arrange
        OwnerInputDto ownerInputDto = OwnerMotherTest.getOwnerInputDto();
        Owner owner = OwnerMotherTest.mapToEntity(ownerInputDto);
        OwnerOutputDto expectedOwnerOutputDto = OwnerMotherTest.mapToDto(owner);
        expectedOwnerOutputDto.setId(ID);

        when(ownerService.save(owner)).thenAnswer(invocation -> {
            Owner ownerArgument = invocation.getArgument(0);
            ownerArgument.setId(ID);
            return ownerArgument;
        });

        // Act
        OwnerOutputDto result = ownerController.create(ownerInputDto);

        // Assert
        assertEquals(expectedOwnerOutputDto, result);
    }

    @Test
    void Given_Valid_OwnerId_And_Valid_OwnerInputDto_When_Update_Then_Return_Updated_OwnerOutputDto() {
        // Arrange
        OwnerInputDto ownerInputDto = OwnerMotherTest.getUpdatedOwnerInputDto();
        Owner updatedOwner = OwnerMotherTest.getUpdatedOwnerWithId(ID, ownerInputDto);
        OwnerOutputDto expectedOwnerOutputDto = OwnerMotherTest.mapToDto(updatedOwner);
        Owner existingOwner = OwnerMotherTest.getOwnerWithId(ID);
        when(ownerRepository.save(updatedOwner)).thenReturn(updatedOwner);
        when(ownerRepository.findByIdOrFail(ID)).thenReturn(existingOwner);

        // Act
        OwnerOutputDto result = ownerController.update(ID, ownerInputDto);

        // Assert
        assertEquals(expectedOwnerOutputDto, result);
        verify(ownerRepository, times(1)).save(updatedOwner);
        verify(ownerRepository, times(1)).findByIdOrFail(ID);
    }

    @Test
    void Given_inexistent_OwnerId_And_Valid_OwnerInputDto_When_Update_Then_Throw_EntityNotFoundException() {
        // Arrange
        OwnerInputDto ownerInputDto = OwnerMotherTest.getUpdatedOwnerInputDto();
        when(ownerRepository.findByIdOrFail(ID)).thenThrow(EntityNotFoundException.class);
        when(ownerRepository.save(any(Owner.class))).thenReturn(any(Owner.class));

        // Act
        EntityNotFoundException e = catchThrowableOfType(() -> ownerController.update(ID, ownerInputDto), EntityNotFoundException.class);

        // Assert
        assertNotNull(e);
        verify(ownerRepository, times(1)).findByIdOrFail(ID);
        verify(ownerRepository, never()).save(any(Owner.class));
    }

    @Test
    void Given_existing_OwnerId_when_delete_then_return_void() {
        // Arrange
        Owner owner = OwnerMotherTest.getExistingOwner();
        when(ownerRepository.findByIdOrFail(ID)).thenReturn(owner);

        // Act
        ownerController.delete(ID);

        // Assert
        verify(ownerRepository, times(1)).findByIdOrFail(ID);
        verify(ownerRepository, times(1)).delete(owner);
    }

    @Test
    void Given_inexistent_OwnerId_when_delete_then_throw_EntityNotFoundException() {
        // Arrange
        when(ownerRepository.findByIdOrFail(ID)).thenThrow(EntityNotFoundException.class);

        // Act
        EntityNotFoundException e = catchThrowableOfType(() -> ownerController.delete(ID), EntityNotFoundException.class);

        // Assert
        assertNotNull(e);
        verify(ownerRepository, times(1)).findByIdOrFail(ID);
        verify(ownerRepository, never()).delete(any(Owner.class));
    }
}