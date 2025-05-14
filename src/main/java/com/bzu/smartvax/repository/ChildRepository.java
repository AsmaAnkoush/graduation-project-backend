package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Child;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Child entity.
 */
@Repository
public interface ChildRepository extends JpaRepository<Child, String> {
    List<Child> findByParentId(Long parentId); // 🔍 لجلب الأطفال حسب الوالد
}
