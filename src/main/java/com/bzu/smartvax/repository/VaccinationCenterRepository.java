package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.VaccinationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationCenterRepository extends JpaRepository<VaccinationCenter, Long> {}
