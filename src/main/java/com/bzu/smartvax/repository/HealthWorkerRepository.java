package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.HealthWorker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HealthWorker entity.
 */
@Repository
public interface HealthWorkerRepository extends JpaRepository<HealthWorker, Long> {
    // بدل البحث بـ userId، نبحث باستخدام reference_id = health_worker.id
    default Optional<HealthWorker> findByUserReferenceId(Long referenceId) {
        return findById(referenceId); // لأن referenceId = healthWorker.id
    }

    List<HealthWorker> findByVaccinationCenterId(Long centerId);
}
