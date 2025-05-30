package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineTypeRepository extends JpaRepository<VaccineType, Long> {}
