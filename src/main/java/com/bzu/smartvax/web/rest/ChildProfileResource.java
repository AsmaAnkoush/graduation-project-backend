package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.Parent;
import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.repository.UsersRepository;
import com.bzu.smartvax.service.dto.ChildProfileDTO;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChildProfileResource {

    private final UsersRepository usersRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    public ChildProfileResource(UsersRepository usersRepository, ParentRepository parentRepository, ChildRepository childRepository) {
        this.usersRepository = usersRepository;
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }

    // ✅ 1. Get child profile
    @GetMapping("/child-profile")
    public ResponseEntity<?> getChildProfile(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("🟡 [GET] child-profile | session userId = " + userId);
        if (userId == null) return ResponseEntity.status(401).body("غير مصرح");

        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("المستخدم غير موجود");

        Optional<Parent> parentOpt = parentRepository.findByUser(userOpt.get());
        if (parentOpt.isEmpty()) return ResponseEntity.badRequest().body("لم يتم العثور على ولي الأمر");

        Parent parent = parentOpt.get();
        // Optional<Child> childOpt = childRepository.findFirstByParent_Id(parent.getId());
        Optional<Child> childOpt = childRepository.findFirstByParentIdWithVaccinationCenter(parent.getId());

        if (childOpt.isEmpty()) return ResponseEntity.badRequest().body("لم يتم العثور على طفل");

        Child child = childOpt.get();

        ChildProfileDTO dto = new ChildProfileDTO();
        dto.id = child.getId();
        dto.name = child.getName() != null ? child.getName() : "";
        dto.dob = child.getDob();
        dto.gender = "ذكر"; // ثابت مؤقتًا
        dto.weight = child.getWeight() != null ? child.getWeight() : BigDecimal.ZERO;
        dto.height = child.getHeight() != null ? child.getHeight() : BigDecimal.ZERO;
        dto.address = (child.getVaccinationCenter() != null && child.getVaccinationCenter().getName() != null)
            ? child.getVaccinationCenter().getName()
            : "";
        dto.parentName = parent.getName() != null ? parent.getName() : "";
        dto.phone = parent.getPhone() != null ? parent.getPhone() : "";

        return ResponseEntity.ok(dto);
    }

    // ✅ 2. Update child profile
    @PutMapping("/update-child-profile")
    public ResponseEntity<?> updateChildProfile(@RequestBody ChildProfileDTO dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("🟡 [PUT] update-child-profile | session userId = " + userId);
        if (userId == null) return ResponseEntity.status(401).body("غير مصرح");

        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("المستخدم غير موجود");

        Optional<Parent> parentOpt = parentRepository.findByUser(userOpt.get());
        if (parentOpt.isEmpty()) return ResponseEntity.badRequest().body("ولي الأمر غير موجود");

        Optional<Child> childOpt = childRepository.findById(dto.id);
        if (childOpt.isEmpty()) return ResponseEntity.badRequest().body("الطفل غير موجود");

        Child child = childOpt.get();
        child.setWeight(dto.weight != null ? dto.weight : BigDecimal.ZERO);
        child.setHeight(dto.height != null ? dto.height : BigDecimal.ZERO);

        childRepository.save(child);
        System.out.println("🟡 [put] child-profile | session userId = " + userId);
        return ResponseEntity.ok("تم تحديث بيانات الطفل بنجاح");
    }
}
