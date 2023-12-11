package com.alexmart.traffic.api.dto.vehicle;

import com.alexmart.traffic.api.dto.owner.OwnerIdDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleInputDto {

    @Valid
    @NotNull
    private OwnerIdDto owner;
    @NotBlank
    @Size(max = 20)
    private String make;
    @NotBlank
    @Size(max = 20)
    private String model;
    @NotBlank
    @Pattern(regexp = "[A-Za-z]{1,3}-[A-Za-z]{1,2}-[0-9]{1,4}")
    private String plate;
}