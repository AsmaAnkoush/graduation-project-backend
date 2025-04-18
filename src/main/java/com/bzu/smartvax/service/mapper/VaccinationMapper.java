package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vaccination} and its DTO {@link VaccinationDTO}.
 */
@Mapper(componentModel = "spring")
public interface VaccinationMapper extends EntityMapper<VaccinationDTO, Vaccination> {}
