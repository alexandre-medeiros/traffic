package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.converter.CitationConverter;
import com.alexmart.traffic.api.dto.citation.CitationInputDto;
import com.alexmart.traffic.api.dto.citation.CitationOutputDto;
import com.alexmart.traffic.domain.model.Citation;
import com.alexmart.traffic.domain.services.CitationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/vehicles/{vehicleId}/citations")
public class CitationController {

    private final CitationService citationService;
    private final CitationConverter converter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitationOutputDto create(@PathVariable Long vehicleId, @RequestBody @Valid CitationInputDto citationInputDto) {
        Citation citation = converter.citationDtoToCitation(citationInputDto);
        Citation savedCitation = citationService.createCitation(vehicleId, citation);
        return converter.citationToCitationDto(savedCitation);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CitationOutputDto> findAll(@PathVariable Long vehicleId) {
        return converter.citationListToCitationDtoList(citationService.findAll(vehicleId));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CitationOutputDto find(@PathVariable Long vehicleId, @PathVariable Long id) {
        return converter.citationToCitationDto(citationService.find(vehicleId, id));
    }
}
