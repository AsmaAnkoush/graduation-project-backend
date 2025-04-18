package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.AIAnalyzer;
import com.bzu.smartvax.domain.Feedback;
import com.bzu.smartvax.service.dto.AIAnalyzerDTO;
import com.bzu.smartvax.service.dto.FeedbackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AIAnalyzer} and its DTO {@link AIAnalyzerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AIAnalyzerMapper extends EntityMapper<AIAnalyzerDTO, AIAnalyzer> {
    @Mapping(target = "feedback", source = "feedback", qualifiedByName = "feedbackId")
    AIAnalyzerDTO toDto(AIAnalyzer s);

    @Named("feedbackId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FeedbackDTO toDtoFeedbackId(Feedback feedback);
}
