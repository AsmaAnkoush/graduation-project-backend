package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { VaccinationMapper.class })
public interface ScheduleVaccinationMapperWithName {
    @Mapping(target = "vaccination", source = "vaccination")
    ScheduleVaccinationDTO toDto(ScheduleVaccination entity);
}
