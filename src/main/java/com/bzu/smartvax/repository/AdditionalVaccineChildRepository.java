package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.AdditionalVaccineChild;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalVaccineChildRepository extends JpaRepository<AdditionalVaccineChild, Long> {
    List<AdditionalVaccineChild> findByChildId(String childId);

    List<AdditionalVaccineChild> findByChildIdAndStatus(String childId, String status);
}
