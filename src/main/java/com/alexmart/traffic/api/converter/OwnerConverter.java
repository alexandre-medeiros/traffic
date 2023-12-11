package com.alexmart.traffic.api.converter;

import com.alexmart.traffic.api.dto.owner.OwnerInputDto;
import com.alexmart.traffic.api.dto.owner.OwnerOutputDto;
import com.alexmart.traffic.domain.model.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerConverter {

    OwnerOutputDto ownerToOwnerDto(Owner owner);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "citations", ignore = true)
    Owner ownerDtoToOwner(OwnerInputDto ownerDto);

    List<OwnerOutputDto> ownerListToOwnerDtoList(List<Owner> ownerList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "citations", ignore = true)
    Owner updateOwner(Owner owner, @MappingTarget Owner existingOwner);
}

