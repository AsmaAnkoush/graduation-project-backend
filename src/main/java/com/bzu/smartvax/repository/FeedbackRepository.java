package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByVaccinationId(Long vaccineId, Pageable pageable);
}
