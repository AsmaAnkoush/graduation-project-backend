package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Vaccination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vaccination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {}
