package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.HealthRecord;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.HealthRecordDTO;
import com.bzu.smartvax.service.dto.ParentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {
    @Mapping(target = "healthRecord", source = "healthRecord", qualifiedByName = "healthRecordId")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "parentId")
    ChildDTO toDto(Child s);

    @Named("healthRecordId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HealthRecordDTO toDtoHealthRecordId(HealthRecord healthRecord);

    @Named("parentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParentDTO toDtoParentId(Parent parent);
}
