package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScheduleVaccination} and its DTO {@link ScheduleVaccinationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScheduleVaccinationMapper extends EntityMapper<ScheduleVaccinationDTO, ScheduleVaccination> {
    @Mapping(target = "child", source = "child", qualifiedByName = "childId")
    @Mapping(target = "vaccination", source = "vaccination", qualifiedByName = "vaccinationId")
    ScheduleVaccinationDTO toDto(ScheduleVaccination s);

    @Named("childId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoChildId(Child child);

    @Named("vaccinationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationDTO toDtoVaccinationId(Vaccination vaccination);
}
