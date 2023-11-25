package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.converter.OwnerConverter;
import com.alexmart.traffic.api.dto.owner.OwnerInputDto;
import com.alexmart.traffic.api.dto.owner.OwnerOutputDto;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.services.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerConverter ownerConverter;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OwnerOutputDto> getAllOwner() {
        return ownerConverter.ownerListToOwnerDtoList(ownerService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OwnerOutputDto getOwner(@PathVariable Long id) {
        return ownerConverter.ownerToOwnerDto(ownerService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerOutputDto createOwner(@RequestBody @Valid OwnerInputDto ownerDto) {
        Owner owner = ownerConverter.ownerDtoToOwner(ownerDto);
        return ownerConverter.ownerToOwnerDto(ownerService.save(owner));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OwnerOutputDto updateOwner(@PathVariable Long id, @RequestBody @Valid OwnerInputDto ownerDto) {
        Owner owner = ownerConverter.ownerDtoToOwner(ownerDto);
        return ownerConverter.ownerToOwnerDto(ownerService.update(id, owner));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwner(@PathVariable Long id) {
        ownerService.delete(id);
    }

}
