package com.alexmart.traffic.api.converter;

import com.alexmart.traffic.api.dto.citation.CitationInputDto;
import com.alexmart.traffic.api.dto.citation.CitationOutputDto;
import com.alexmart.traffic.domain.model.Citation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CitationConverter {

    CitationOutputDto citationToCitationDto(Citation citation);

    Citation citationDtoToCitation(CitationInputDto citationDto);

    Citation updateCitation(Citation citation, @MappingTarget Citation existingCitation);

    List<CitationOutputDto> citationListToCitationDtoList(List<Citation> citationList);
}
