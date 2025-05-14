package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ScheduleVaccinationController {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final ScheduleVaccinationRepository scheduleVaccinationRepository;

    public ScheduleVaccinationController(
        ParentRepository parentRepository,
        ChildRepository childRepository,
        ScheduleVaccinationRepository scheduleVaccinationRepository
    ) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.scheduleVaccinationRepository = scheduleVaccinationRepository;
    }

    /**
     * GET /api/schedule-vaccinations
     * يرجع جميع التطعيمات المجدولة للأطفال المرتبطين بالأب المسجل حاليًا
     */
    @GetMapping("/schedule-vaccinations")
    public List<ScheduleVaccination> getAllForParent(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            System.out.println("❌ لا يوجد مستخدم في الجلسة.");
            return Collections.emptyList();
        }

        Optional<Parent> parentOpt = parentRepository.findByUser_Username(user.getUsername());
        if (parentOpt.isEmpty()) {
            System.out.println("❌ لم يتم العثور على الأب.");
            return Collections.emptyList();
        }

        Parent parent = parentOpt.get();

        List<String> childIds = childRepository.findByParentId(parent.getId()).stream().map(Child::getId).collect(Collectors.toList());

        if (childIds.isEmpty()) {
            return Collections.emptyList();
        }

        return scheduleVaccinationRepository.findAllWithVaccinationByChildIds(childIds);
    }
}
