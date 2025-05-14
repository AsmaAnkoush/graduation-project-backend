// ✅ ChildMapper.java (مُعدل لإضافة partialUpdate)
package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.HealthRecord;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.HealthRecordDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChildMapper {
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "healthRecord", target = "healthRecord")
    ChildDTO toDto(Child child);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "healthRecord", target = "healthRecord")
    Child toEntity(ChildDTO childDTO);

    @Mapping(target = "parent", ignore = true)
    void partialUpdate(@MappingTarget Child entity, ChildDTO dto);

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
// ✅ دعم builder-style لـ Child
// في ملف Child.java أضف التالي داخل الكلاس:
// public Child id(String id) {
//     this.setId(id);
//     return this;
// }
// public Child name(String name) {
//     this.setName(name);
//     return this;
// }
// public Child dob(LocalDate dob) {
//     this.setDob(dob);
//     return this;
// }
// public Child parent(Parent parent) {
//     this.setParent(parent);
//     return this;
// }
// public Child healthRecord(HealthRecord healthRecord) {
//     this.setHealthRecord(healthRecord);
//     return this;
// }
