package com.alexmart.traffic.api.dto.citation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class CitationInputDto {

    @NotBlank
    private String description;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    @JsonProperty("date_violation")
    private OffsetDateTime dateViolation;
}
