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
        return new AdditionalVaccineDTO(entity.getId(), entity.getName());
    }

    public AdditionalVaccine toEntity(AdditionalVaccineDTO dto) {
        if (dto == null) return null;
        AdditionalVaccine entity = new AdditionalVaccine();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    public List<AdditionalVaccineDTO> toDtoList(List<AdditionalVaccine> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<AdditionalVaccine> toEntityList(List<AdditionalVaccineDTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
