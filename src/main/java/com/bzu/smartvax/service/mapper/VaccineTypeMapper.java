package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.VaccineType;
import com.bzu.smartvax.service.dto.VaccineTypeDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VaccineTypeMapper extends EntityMapper<VaccineTypeDTO, VaccineType> {}
