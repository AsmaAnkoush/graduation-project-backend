package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Child;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Child entity.
 */
@Repository
public interface ChildRepository extends JpaRepository<Child, String> {
    List<Child> findByParentId(Long parentId); // 🔍 لجلب الأطفال حسب الوالد

    Optional<Child> findFirstByParent_Id(Long parentId);

    @org.springframework.data.jpa.repository.Query(
        "SELECT c FROM Child c LEFT JOIN FETCH c.vaccinationCenter WHERE c.parent.id = :parentId"
    )
    Optional<Child> findFirstByParentIdWithVaccinationCenter(@org.springframework.data.repository.query.Param("parentId") Long parentId);

    @Query("SELECT c FROM Child c LEFT JOIN FETCH c.vaccinationCenter WHERE c.id = :id")
    Optional<Child> findByIdWithVaccinationCenter(@Param("id") String id);

    @Query(
        """
            SELECT c FROM Child c
            LEFT JOIN FETCH c.vaccinationCenter
            LEFT JOIN FETCH c.parent
            LEFT JOIN FETCH c.healthRecord
            WHERE c.id = :id
        """
    )
    Optional<Child> findByIdWithAllRelations(@Param("id") String id);

    List<Child> findByVaccinationCenter_Id(Long centerId);
}
