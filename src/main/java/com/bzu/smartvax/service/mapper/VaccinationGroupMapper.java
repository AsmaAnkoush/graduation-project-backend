package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.VaccinationGroup;
import com.bzu.smartvax.service.dto.VaccinationGroupDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VaccinationGroupMapper extends EntityMapper<VaccinationGroupDTO, VaccinationGroup> {}
