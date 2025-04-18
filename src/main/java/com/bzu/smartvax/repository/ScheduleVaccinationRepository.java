package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.ScheduleVaccination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScheduleVaccination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleVaccinationRepository extends JpaRepository<ScheduleVaccination, Long> {}
