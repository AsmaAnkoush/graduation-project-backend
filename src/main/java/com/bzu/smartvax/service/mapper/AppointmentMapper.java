package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.HealthWorkerDTO;
import com.bzu.smartvax.service.dto.ParentDTO;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "parentId")
    @Mapping(target = "child", source = "child", qualifiedByName = "childId")
    @Mapping(target = "schedule", source = "schedule", qualifiedByName = "scheduleVaccinationId")
    @Mapping(target = "healthWorker", source = "healthWorker", qualifiedByName = "healthWorkerId")
    AppointmentDTO toDto(Appointment s);

    @Named("parentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParentDTO toDtoParentId(Parent parent);

    @Named("childId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoChildId(Child child);

    @Named("scheduleVaccinationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ScheduleVaccinationDTO toDtoScheduleVaccinationId(ScheduleVaccination scheduleVaccination);

    @Named("healthWorkerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HealthWorkerDTO toDtoHealthWorkerId(HealthWorker healthWorker);
}
