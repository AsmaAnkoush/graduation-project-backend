package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.AdditionalVaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalVaccineRepository extends JpaRepository<AdditionalVaccine, Long> {}
