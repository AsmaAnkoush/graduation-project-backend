package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.service.dto.HealthWorkerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HealthWorker} and its DTO {@link HealthWorkerDTO}.
 */
@Mapper(componentModel = "spring")
public interface HealthWorkerMapper extends EntityMapper<HealthWorkerDTO, HealthWorker> {}
