package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.HealthRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HealthRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {}
