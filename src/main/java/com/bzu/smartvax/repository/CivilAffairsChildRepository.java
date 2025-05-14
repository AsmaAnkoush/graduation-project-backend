package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.CivilAffairsChild;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CivilAffairsChildRepository extends JpaRepository<CivilAffairsChild, String> {
    // البحث بناءً على الهوية
    Optional<CivilAffairsChild> findById(String id);

    // البحث بناءً على الهوية والاسم وتاريخ الميلاد ورقم الهاتف
    Optional<CivilAffairsChild> findByIdAndNameAndDobAndPhone(String id, String name, java.time.LocalDate dob, String phone);
}
