package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.AdditionalVaccine;
import com.bzu.smartvax.domain.AdditionalVaccineChild;
import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.service.dto.AdditionalVaccineChildDTO;
import com.bzu.smartvax.service.dto.AdditionalVaccineDTO;
import com.bzu.smartvax.service.dto.ChildDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdditionalVaccineChildMapper {
    @Mapping(source = "child", target = "child")
    @Mapping(source = "additionalVaccine", target = "additionalVaccine")
    AdditionalVaccineChildDTO toDto(AdditionalVaccineChild entity);

    @Mapping(source = "child", target = "child")
    @Mapping(source = "additionalVaccine", target = "additionalVaccine")
    AdditionalVaccineChild toEntity(AdditionalVaccineChildDTO dto);

    default Child fromChildDto(ChildDTO dto) {
        if (dto == null) return null;
        Child child = new Child();
        child.setId(dto.getId());
        return child;
    }

    default ChildDTO toChildDto(Child entity) {
        if (entity == null) return null;
        ChildDTO dto = new ChildDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    default AdditionalVaccine fromAdditionalDto(AdditionalVaccineDTO dto) {
        if (dto == null) return null;
        AdditionalVaccine vaccine = new AdditionalVaccine();
        vaccine.setId(dto.getId());
        return vaccine;
    }

    default AdditionalVaccineDTO toAdditionalDto(AdditionalVaccine entity) {
        if (entity == null) return null;
        AdditionalVaccineDTO dto = new AdditionalVaccineDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
