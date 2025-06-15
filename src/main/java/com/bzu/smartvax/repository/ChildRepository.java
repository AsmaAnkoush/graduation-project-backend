package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Child;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
