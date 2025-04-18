package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.HealthWorker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HealthWorker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HealthWorkerRepository extends JpaRepository<HealthWorker, Long> {}
