package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.HealthRecord;
import com.bzu.smartvax.service.dto.HealthRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HealthRecord} and its DTO {@link HealthRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface HealthRecordMapper extends EntityMapper<HealthRecordDTO, HealthRecord> {}
