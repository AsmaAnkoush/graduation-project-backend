package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.ChildGrowthRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildGrowthRecordRepository extends JpaRepository<ChildGrowthRecord, Long> {
    // ✅ الصحيح: child هو object وليس field مباشرة
    List<ChildGrowthRecord> findByChild_Id(String childId);
}
