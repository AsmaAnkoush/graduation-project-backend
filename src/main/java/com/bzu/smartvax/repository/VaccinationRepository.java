package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.domain.VaccineType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    @Query("SELECT v FROM Vaccination v LEFT JOIN FETCH v.group WHERE v.id = :id")
    Optional<Vaccination> findByIdWithGroup(@Param("id") Long id);

    @Query("SELECT v FROM Vaccination v LEFT JOIN FETCH v.group LEFT JOIN FETCH v.vaccineType WHERE v.id = :id")
    Optional<Vaccination> findByIdWithGroupAndVaccineType(@Param("id") Long id);
}
