package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vaccination} and its DTO {@link VaccinationDTO}.
 */
@Mapper(componentModel = "spring")
public interface VaccinationMapper extends EntityMapper<VaccinationDTO, Vaccination> {
    @Override
    @Mapping(source = "vaccineType.id", target = "vaccineTypeId")
    @Mapping(source = "vaccineType.name", target = "vaccineTypeName")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "group.targetAgeDays", target = "targetAgeDays")
    VaccinationDTO toDto(Vaccination vaccination);

    @Override
    @Mapping(source = "vaccineTypeId", target = "vaccineType.id")
    @Mapping(source = "groupId", target = "group.id")
    Vaccination toEntity(VaccinationDTO dto);

    default Vaccination fromId(Long id) {
        if (id == null) return null;
        Vaccination vaccination = new Vaccination();
        vaccination.setId(id);
        return vaccination;
    }
}
