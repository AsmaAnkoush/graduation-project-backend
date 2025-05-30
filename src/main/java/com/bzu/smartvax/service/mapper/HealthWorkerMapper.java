package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.domain.VaccinationCenter;
import com.bzu.smartvax.service.dto.HealthWorkerDTO;
import com.bzu.smartvax.service.dto.VaccinationCenterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HealthWorker} and its DTO {@link HealthWorkerDTO}.
 */
@Mapper(componentModel = "spring")
public interface HealthWorkerMapper extends EntityMapper<HealthWorkerDTO, HealthWorker> {
    @Mapping(target = "vaccinationCenter", source = "vaccinationCenter", qualifiedByName = "vaccinationCenterId")
    HealthWorkerDTO toDto(HealthWorker healthWorker);

    @Named("vaccinationCenterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationCenterDTO toDtoVaccinationCenterId(VaccinationCenter vaccinationCenter);
}
