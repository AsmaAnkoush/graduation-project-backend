package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.VaccinationCenter;
import com.bzu.smartvax.service.dto.VaccinationCenterDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VaccinationCenterMapper extends EntityMapper<VaccinationCenterDTO, VaccinationCenter> {
    @Named("vaccinationCenterFull")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    VaccinationCenterDTO toDtoVaccinationCenterFull(VaccinationCenter center);
}
