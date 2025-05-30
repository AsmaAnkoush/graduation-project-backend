package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.domain.VaccinationGroup;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.dto.VaccinationGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScheduleVaccination} and its DTO {@link ScheduleVaccinationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScheduleVaccinationMapper extends EntityMapper<ScheduleVaccinationDTO, ScheduleVaccination> {
    @Mapping(target = "child", source = "child", qualifiedByName = "childId")
    @Mapping(target = "vaccination", source = "vaccination", qualifiedByName = "vaccinationId")
    @Mapping(target = "vaccinationGroup", source = "vaccinationGroup", qualifiedByName = "groupId") // ✅ الجديد
    ScheduleVaccinationDTO toDto(ScheduleVaccination s);

    // === Child mapping ===
    @Named("childId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoChildId(Child child);

    // === Vaccination mapping ===
    @Named("vaccinationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationDTO toDtoVaccinationId(Vaccination vaccination);

    // === VaccinationGroup mapping ===
    @Named("groupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationGroupDTO toDtoGroupId(VaccinationGroup group); // ✅ الجديد
}
