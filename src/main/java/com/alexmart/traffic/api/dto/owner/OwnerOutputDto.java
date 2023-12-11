package com.alexmart.traffic.api.dto.owner;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class OwnerOutputDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
