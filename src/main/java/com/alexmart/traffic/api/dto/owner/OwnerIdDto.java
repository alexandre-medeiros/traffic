package com.alexmart.traffic.api.dto.owner;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerIdDto {

    @NotNull
    private Long id;
    private String name;
}
