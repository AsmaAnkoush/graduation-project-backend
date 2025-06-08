package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.HealthRecord;
import com.bzu.smartvax.domain.VaccinationCenter;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.HealthRecordDTO;
import com.bzu.smartvax.service.dto.VaccinationCenterDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChildMapper {
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "healthRecord", target = "healthRecord")
    @Mapping(source = "vaccinationCenter", target = "vaccinationCenter", qualifiedByName = "vaccinationCenterId")
    ChildDTO toDto(Child child);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "healthRecord", target = "healthRecord")
    @Mapping(source = "vaccinationCenter", target = "vaccinationCenter")
    Child toEntity(ChildDTO childDTO);

    @Named("childMini")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name") // ✅ تطابق الاسم مع الموجود في ChildDTO
    ChildDTO toMiniDto(Child child);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "vaccinationCenter", ignore = true)
    void partialUpdate(@MappingTarget Child entity, ChildDTO dto);

    // MapStruct helper for VaccinationCenterDTO
    @Named("vaccinationCenterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VaccinationCenterDTO toDtoVaccinationCenterId(VaccinationCenter center);

    // Manual mappers for HealthRecord
    default HealthRecordDTO map(HealthRecord record) {
        if (record == null) return null;
        HealthRecordDTO dto = new HealthRecordDTO();
        dto.setId(record.getId());
        dto.setSensitivity(record.getSensitivity());
        dto.setDiabetes(record.getDiabetes());
        dto.setHighBloodPressure(record.getHighBloodPressure());
        dto.setGeneticDiseases(record.getGeneticDiseases());
        dto.setBloodType(record.getBloodType());
        return dto;
    }

    default HealthRecord map(HealthRecordDTO dto) {
        if (dto == null) return null;
        HealthRecord record = new HealthRecord();
        record.setId(dto.getId());
        record.setSensitivity(dto.getSensitivity());
        record.setDiabetes(dto.getDiabetes());
        record.setHighBloodPressure(dto.getHighBloodPressure());
        record.setGeneticDiseases(dto.getGeneticDiseases());
        record.setBloodType(dto.getBloodType());
        return record;
    }
}
