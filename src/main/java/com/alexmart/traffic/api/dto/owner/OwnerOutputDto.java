package com.alexmart.traffic.api.dto.owner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerOutputDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
