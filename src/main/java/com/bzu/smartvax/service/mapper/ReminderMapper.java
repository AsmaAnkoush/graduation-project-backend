package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.Reminder;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.dto.ParentDTO;
import com.bzu.smartvax.service.dto.ReminderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reminder} and its DTO {@link ReminderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReminderMapper extends EntityMapper<ReminderDTO, Reminder> {
    @Mapping(target = "appointment", source = "appointment", qualifiedByName = "appointmentId")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "parentId")
    ReminderDTO toDto(Reminder s);

    @Named("appointmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentDTO toDtoAppointmentId(Appointment appointment);

    @Named("parentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParentDTO toDtoParentId(Parent parent);
}
