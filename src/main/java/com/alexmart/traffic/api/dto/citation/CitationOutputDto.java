package com.alexmart.traffic.api.dto.citation;

import com.alexmart.traffic.api.dto.vehicle.VehicleOutputDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CitationOutputDto {

    private Long id;
    private VehicleOutputDto vehicle;
    private String description;
    private BigDecimal amount;
    private OffsetDateTime dateViolation;
}
