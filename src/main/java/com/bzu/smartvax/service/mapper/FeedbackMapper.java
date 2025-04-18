package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Feedback;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.service.dto.FeedbackDTO;
import com.bzu.smartvax.service.dto.ParentDTO;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "parentId")
    @Mapping(target = "vaccination", source = "vaccination", qualifiedByName = "vaccinationId")
    FeedbackDTO toDto(Feedback s);

    @Named("parentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ParentDTO toDtoParentId(Parent parent);

    @Named("vaccinationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationDTO toDtoVaccinationId(Vaccination vaccination);
}
