package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.*;
import com.bzu.smartvax.service.dto.*;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChildMapper.class })
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "parentId")
    @Mapping(target = "child", source = "child", qualifiedByName = "childMini") // ✅ استخدم mini mapper لعرض الاسم والرقم
    @Mapping(target = "healthWorker", source = "healthWorker", qualifiedByName = "healthWorkerId")
    @Mapping(target = "vaccinationCenter", source = "vaccinationCenter", qualifiedByName = "vaccinationCenterFull")
    @Mapping(target = "requestedNewCenter", source = "requestedNewCenter", qualifiedByName = "vaccinationCenterFull")
    @Mapping(target = "scheduleVaccinations", source = "schedules", qualifiedByName = "toScheduleVaccinationDtoList")
    @Mapping(target = "rescheduleReason", source = "rescheduleReason")
    AppointmentDTO toDto(Appointment s);

    @Mapping(target = "parent", source = "parent")
    @Mapping(target = "child", source = "child")
    @Mapping(target = "healthWorker", source = "healthWorker")
    @Mapping(target = "vaccinationCenter", source = "vaccinationCenter")
    @Mapping(target = "requestedNewCenter", source = "requestedNewCenter", qualifiedByName = "vaccinationCenterId")
    @Mapping(target = "schedules", source = "scheduleVaccinations", qualifiedByName = "toScheduleVaccinationList")
    @Mapping(target = "rescheduleReason", source = "rescheduleReason")
    Appointment toEntity(AppointmentDTO dto);

    // ========== MINI DTO MAPPERS ==========

    @Named("parentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParentDTO toDtoParentId(Parent parent);

    @Named("healthWorkerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HealthWorkerDTO toDtoHealthWorkerId(HealthWorker healthWorker);

    @Named("vaccinationCenterFull")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "location", source = "location")
    VaccinationCenterDTO toDtoVaccinationCenterFull(VaccinationCenter center);

    @Named("vaccinationCenterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationCenter toVaccinationCenterFromId(VaccinationCenterDTO dto);

    // ========== ScheduleVaccination Mapper ===========

    @Named("toScheduleVaccinationDtoList")
    default List<ScheduleVaccinationDTO> toScheduleVaccinationDtoList(List<ScheduleVaccination> schedules) {
        if (schedules == null) return null;
        return schedules.stream().map(this::mapScheduleToDTO).collect(Collectors.toList());
    }

    default ScheduleVaccinationDTO mapScheduleToDTO(ScheduleVaccination schedule) {
        if (schedule == null) return null;

        ScheduleVaccinationDTO dto = new ScheduleVaccinationDTO();
        dto.setId(schedule.getId());
        dto.setScheduledDate(schedule.getScheduledDate());
        dto.setStatus(schedule.getStatus());

        if (schedule.getVaccination() != null) {
            VaccinationDTO vaccinationDTO = new VaccinationDTO();
            vaccinationDTO.setId(schedule.getVaccination().getId());
            vaccinationDTO.setName(schedule.getVaccination().getName());

            if (schedule.getVaccination().getVaccineType() != null) {
                VaccineTypeDTO typeDTO = new VaccineTypeDTO();
                typeDTO.setId(schedule.getVaccination().getVaccineType().getId());
                typeDTO.setName(schedule.getVaccination().getVaccineType().getName());
                vaccinationDTO.setVaccineType(typeDTO);
            }

            dto.setVaccination(vaccinationDTO);
        }

        return dto;
    }

    @Named("toScheduleVaccinationList")
    default List<ScheduleVaccination> toScheduleVaccinationList(List<ScheduleVaccinationDTO> dtos) {
        if (dtos == null) return null;
        return dtos
            .stream()
            .map(dto -> {
                ScheduleVaccination schedule = new ScheduleVaccination();
                schedule.setId(dto.getId());
                return schedule;
            })
            .collect(Collectors.toList());
    }
}
