package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.Reminder;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.dto.ParentDTO;
import com.bzu.smartvax.service.dto.ReminderDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReminderMapper extends EntityMapper<ReminderDTO, Reminder> {
    @Mapping(target = "appointment", source = "appointment", qualifiedByName = "appointmentId")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "parentId")
    @Mapping(target = "scheduledDate", source = "scheduledDate")
    @Mapping(target = "sent", source = "sent")
    @Mapping(target = "recipientType", source = "recipientType")
    @Mapping(target = "childId", source = "child.id")
    @Mapping(source = "type", target = "type")
    @Mapping(
        target = "handledByWorkerId",
        expression = "java(reminder.getHandledByWorker() != null ? reminder.getHandledByWorker().getId() : null)"
    )
    @Mapping(target = "handledDate", source = "handledDate")
    @Mapping(source = "parentViewed", target = "parentViewed")
    ReminderDTO toDto(Reminder reminder);

    @Mapping(target = "appointment", source = "appointment")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "scheduledDate", source = "scheduledDate")
    @Mapping(target = "sent", source = "sent")
    @Mapping(target = "recipientType", source = "recipientType")
    @Mapping(target = "child", source = "childId", qualifiedByName = "childFromId")
    @Mapping(source = "type", target = "type")
    @Mapping(target = "handledByWorker", source = "handledByWorkerId", qualifiedByName = "workerFromId")
    @Mapping(target = "handledDate", source = "handledDate")
    @Mapping(source = "parentViewed", target = "parentViewed")
    Reminder toEntity(ReminderDTO dto);

    @Named("appointmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentDTO toDtoAppointmentId(Appointment appointment);

    @Named("parentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParentDTO toDtoParentId(Parent parent);

    @Named("childFromId")
    default com.bzu.smartvax.domain.Child childFromId(String id) {
        if (id == null) return null;
        com.bzu.smartvax.domain.Child child = new com.bzu.smartvax.domain.Child();
        child.setId(id);
        return child;
    }

    @Named("workerFromId")
    default com.bzu.smartvax.domain.HealthWorker workerFromId(Long id) {
        if (id == null) return null;
        com.bzu.smartvax.domain.HealthWorker worker = new com.bzu.smartvax.domain.HealthWorker();
        worker.setId(id);
        return worker;
    }
}
//package com.bzu.smartvax.service.mapper;
//
//import com.bzu.smartvax.domain.Appointment;
//import com.bzu.smartvax.domain.Parent;
//import com.bzu.smartvax.domain.Reminder;
//import com.bzu.smartvax.service.dto.AppointmentDTO;
//import com.bzu.smartvax.service.dto.ParentDTO;
//import com.bzu.smartvax.service.dto.ReminderDTO;
//import org.mapstruct.*;
//
///**
// * Mapper for the entity {@link Reminder} and its DTO {@link ReminderDTO}.
// */
//
//@Mapper(componentModel = "spring")
//public interface ReminderMapper extends EntityMapper<ReminderDTO, Reminder> {
//    @Mapping(target = "appointment", source = "appointment", qualifiedByName = "appointmentId")
//    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "parentId")
//    @Mapping(target = "scheduledDate", source = "scheduledDate")
//    @Mapping(target = "sent", source = "sent")
//    @Mapping(target = "recipientType", source = "recipientType")
//    @Mapping(target = "childId", source = "child.id")
//    @Mapping(source = "type", target = "type")
////    @Mapping(target = "handledByWorkerId", source = "handledByWorker.id")
//    @Mapping(target = "handledByWorkerId", expression = "java(reminder.getHandledByWorker() != null ? reminder.getHandledByWorker().getId() : null)")
//
//    @Mapping(target = "handledDate", source = "handledDate")
//
//    ReminderDTO toDto(Reminder reminder);
//
//
//    @Mapping(target = "appointment", source = "appointment")
//    @Mapping(target = "recipient", source = "recipient")
//    @Mapping(target = "scheduledDate", source = "scheduledDate")
//    @Mapping(target = "sent", source = "sent")
//    @Mapping(target = "recipientType", source = "recipientType")
//    @Mapping(target = "child", source = "childId", qualifiedByName = "childFromId")
//    @Mapping(source = "type", target = "type")
//    @Mapping(target = "handledByWorker", source = "handledByWorkerId", qualifiedByName = "workerFromId")
//    @Mapping(target = "handledDate", source = "handledDate")
//    Reminder toEntity(ReminderDTO dto);
//
//    @Named("appointmentId")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    AppointmentDTO toDtoAppointmentId(Appointment appointment);
//
//    @Named("parentId")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    ParentDTO toDtoParentId(Parent parent);
//
//    @Named("childFromId")
//    default com.bzu.smartvax.domain.Child childFromId(String id) {
//        if (id == null) return null;
//        com.bzu.smartvax.domain.Child child = new com.bzu.smartvax.domain.Child();
//        child.setId(id);
//        return child;
//    }
//    @Named("workerFromId")
//    default com.bzu.smartvax.domain.HealthWorker workerFromId(Long id) {
//        if (id == null) return null;
//        com.bzu.smartvax.domain.HealthWorker worker = new com.bzu.smartvax.domain.HealthWorker();
//        worker.setId(id);
//        return worker;
//    }
//
//}
