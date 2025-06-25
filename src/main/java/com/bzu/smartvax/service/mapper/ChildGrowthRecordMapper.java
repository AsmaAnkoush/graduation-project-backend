package com.bzu.smartvax.service.mapper;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.ChildGrowthRecord;
import com.bzu.smartvax.service.dto.ChildGrowthRecordDTO;
import org.springframework.stereotype.Component;

@Component
public class ChildGrowthRecordMapper {

    // من Entity إلى DTO
    public ChildGrowthRecordDTO toDto(ChildGrowthRecord entity) {
        if (entity == null) return null;

        ChildGrowthRecordDTO dto = new ChildGrowthRecordDTO();
        dto.setChildId(entity.getChild().getId());
        dto.setHeight(entity.getHeight());
        dto.setWeight(entity.getWeight());
        dto.setAgeInDays(entity.getAgeInDays());
        dto.setDateRecorded(entity.getDateRecorded());

        // حساب العمر بالشهور لتوليد القيم المرجعية ديناميكيًا
        double ageInMonths = entity.getAgeInDays() / 30.0;

        dto.setMinWeight((ageInMonths * 0.5) + 3 - 1.5);
        dto.setMaxWeight((ageInMonths * 0.5) + 3 + 1.5);

        dto.setMinHeight((ageInMonths * 2.5) + 49 - 2);
        dto.setMaxHeight((ageInMonths * 2.5) + 49 + 2);

        return dto;
    }

    // من DTO إلى Entity (يتطلب إدخال كائن الطفل جاهز)
    public ChildGrowthRecord toEntity(ChildGrowthRecordDTO dto, Child child) {
        if (dto == null || child == null) return null;

        ChildGrowthRecord entity = new ChildGrowthRecord();
        entity.setChild(child);
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());

        // العمر والتاريخ تُحسب في الخدمة وليس هنا
        return entity;
    }
}
