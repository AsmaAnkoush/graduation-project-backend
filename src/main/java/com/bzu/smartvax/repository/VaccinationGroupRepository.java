package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.VaccinationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationGroupRepository extends JpaRepository<VaccinationGroup, Long> {}
