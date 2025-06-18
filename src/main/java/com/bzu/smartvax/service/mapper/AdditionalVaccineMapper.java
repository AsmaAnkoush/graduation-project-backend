package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.AdditionalVaccine;
import com.bzu.smartvax.service.dto.AdditionalVaccineDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AdditionalVaccineMapper {

    public AdditionalVaccineDTO toDto(AdditionalVaccine entity) {
        if (entity == null) return null;

        AdditionalVaccineDTO dto = new AdditionalVaccineDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setMinAgeMonths(entity.getMinAgeMonths());
        dto.setDoseCount(entity.getDoseCount());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    public AdditionalVaccine toEntity(AdditionalVaccineDTO dto) {
        if (dto == null) return null;

        AdditionalVaccine entity = new AdditionalVaccine();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setMinAgeMonths(dto.getMinAgeMonths());
        entity.setDoseCount(dto.getDoseCount());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    public List<AdditionalVaccineDTO> toDtoList(List<AdditionalVaccine> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AdditionalVaccine> toEntityList(List<AdditionalVaccineDTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
