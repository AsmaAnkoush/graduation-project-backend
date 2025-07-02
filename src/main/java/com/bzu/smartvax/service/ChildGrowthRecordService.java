package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.ChildGrowthRecord;
import com.bzu.smartvax.repository.ChildGrowthRecordRepository;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.service.dto.ChildGrowthRecordDTO;
import com.bzu.smartvax.service.mapper.ChildGrowthRecordMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChildGrowthRecordService {

    private final ChildGrowthRecordRepository growthRepo;
    private final ChildRepository childRepo;
    private final ChildGrowthRecordMapper mapper;

    public ChildGrowthRecordService(ChildGrowthRecordRepository growthRepo, ChildRepository childRepo, ChildGrowthRecordMapper mapper) {
        this.growthRepo = growthRepo;
        this.childRepo = childRepo;
        this.mapper = mapper;
    }

    public String analyzeAndSave(ChildGrowthRecordDTO dto) {
        Child child = childRepo.findById(dto.getChildId()).orElseThrow(() -> new IllegalArgumentException("Child not found"));

        long ageInDays = ChronoUnit.DAYS.between(child.getDob(), LocalDate.now());
        double ageInMonths = ageInDays / 30.0;

        double expectedWeight = (ageInMonths * 0.5) + 3;
        double expectedHeight = (ageInMonths * 2.5) + 49;

        String weightStatus = (dto.getWeight() < expectedWeight - 1.5)
            ? "❗ وزن الطفل أقل من الطبيعي."
            : (dto.getWeight() > expectedWeight + 1.5) ? "⚠️ وزن الطفل أعلى من الطبيعي." : "✅ وزن الطفل طبيعي.";

        String heightStatus = (dto.getHeight() < expectedHeight - 2)
            ? "❗ طول الطفل أقل من الطبيعي."
            : (dto.getHeight() > expectedHeight + 2) ? "⚠️ طول الطفل أعلى من الطبيعي." : "✅ طول الطفل طبيعي.";

        ChildGrowthRecord record = new ChildGrowthRecord();
        record.setChild(child);
        record.setWeight(dto.getWeight());
        record.setHeight(dto.getHeight());
        record.setAgeInDays((int) ageInDays);
        record.setDateRecorded(LocalDateTime.now());

        growthRepo.save(record);

        return weightStatus + " " + heightStatus;
    }

    public ChildGrowthRecordDTO getReferenceForAge(int ageInDays) {
        double ageInMonths = ageInDays / 30.0;

        double expectedWeight = (ageInMonths * 0.5) + 3;
        double expectedHeight = (ageInMonths * 2.5) + 49;

        double minWeight = expectedWeight - 1.5;
        double maxWeight = expectedWeight + 1.5;

        double minHeight = expectedHeight - 2;
        double maxHeight = expectedHeight + 2;

        return new ChildGrowthRecordDTO(minWeight, maxWeight, minHeight, maxHeight);
    }

    // ✅ استرجاع كل القياسات السابقة لطفل
    public List<ChildGrowthRecordDTO> getAllRecordsForChild(String childId) {
        List<ChildGrowthRecord> records = growthRepo.findByChild_Id(childId);
        return records.stream().map(mapper::toDto).toList();
    }
}
