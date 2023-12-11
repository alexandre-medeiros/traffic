package com.alexmart.traffic.modeltest;

import com.alexmart.traffic.api.dto.owner.OwnerInputDto;
import com.alexmart.traffic.api.dto.owner.OwnerOutputDto;
import com.alexmart.traffic.domain.model.Owner;
import java.util.Arrays;
import java.util.List;
public class OwnerMotherTest {

    private OwnerMotherTest() {
    }

    public static Owner getNewOwner() {
        return new OwnerBuilderTest()
                .withName("John Doe")
                .withEmail("john@example.com")
                .withPhone("1234567890")
                .build();
    }

    public static OwnerInputDto getUpdatedOwnerInputDto() {
        return new OwnerInputDto("John Doe Updated", "john.updated@example.com", "1239999999");
    }

    public static Owner getUpdatedOwnerWithId(Long id, OwnerInputDto ownerInputDto) {
        return new OwnerBuilderTest()
                .withId(id)
                .withName(ownerInputDto.getName())
                .withEmail(ownerInputDto.getEmail())
                .withPhone(ownerInputDto.getPhone())
                .build();
    }

    public static Owner getUpdatedOwnerWithId(Long id) {
        return new OwnerBuilderTest()
                .withId(id)
                .withName("John Doe Updated")
                .withEmail("john.updated@example.com")
                .withPhone("1239999999")
                .build();
    }

    public static Owner getNewOwnerWithEmail(String email) {
        return new OwnerBuilderTest()
                .withName("John Doe")
                .withEmail(email)
                .withPhone("1234567890")
                .build();
    }

    public static Owner getExistingOwner() {
        return new OwnerBuilderTest()
                .withId(1L)
                .withName("John Doe")
                .withEmail("john@example.com")
                .withPhone("1234567890")
                .build();
    }

    public static Owner getExistingOwnerWithEmail(String email) {
        return new OwnerBuilderTest()
                .withId(1L)
                .withName("John Doe")
                .withEmail(email)
                .withPhone("1234567890")
                .build();
    }

    public static Owner getOwnerWithId(Long id) {
        Owner owner = getNewOwner();
        owner.setId(id);
        return owner;
    }


    public static OwnerOutputDto mapToDto(Owner owner) {
        OwnerOutputDto dto = new OwnerOutputDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setEmail(owner.getEmail());
        dto.setPhone(owner.getPhone());
        return dto;
    }

    public static Owner mapToEntity(OwnerInputDto owner) {
        Owner entity = new Owner();
        entity.setName(owner.getName());
        entity.setEmail(owner.getEmail());
        entity.setPhone(owner.getPhone());
        return entity;
    }

    public static List<OwnerOutputDto> mapToDtoList(List<Owner> owners) {
        return owners
                .stream()
                .map(OwnerMotherTest::mapToDto)
                .toList();
    }

    public static List<Owner> getExistingOwnerList() {
        Owner owner1 = new Owner(1L, "John Doe", "john@example.com", "123-456-7890");
        Owner owner2 = new Owner(2L, "Jane Doe", "jane@example.com", "987-654-3210");
        Owner owner3 = new Owner(3L, "Bob Smith", "bob@example.com", "111-222-3333");

        List<Owner> owners = Arrays.asList(owner1, owner2, owner3);

        return owners;
    }

    public static OwnerInputDto getOwnerInputDto() {
        return new OwnerInputDto("John Doe", "john@example.com", "1234567890");
    }

}
