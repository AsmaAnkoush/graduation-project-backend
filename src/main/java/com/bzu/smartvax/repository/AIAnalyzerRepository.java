package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.AIAnalyzer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AIAnalyzer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AIAnalyzerRepository extends JpaRepository<AIAnalyzer, Long> {}
