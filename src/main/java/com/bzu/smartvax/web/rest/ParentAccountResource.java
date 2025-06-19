package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.*;
import com.bzu.smartvax.repository.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ParentAccountResource {

    private final UsersRepository usersRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final VaccinationCenterRepository centerRepository;
    private final PasswordEncoder passwordEncoder;

    public ParentAccountResource(
        UsersRepository usersRepository,
        ParentRepository parentRepository,
        ChildRepository childRepository,
        VaccinationCenterRepository centerRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usersRepository = usersRepository;
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.centerRepository = centerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 1. جلب بيانات الحساب الحالي
    @GetMapping("/parent-account")
    public ResponseEntity<?> getAccount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Optional<Users> userOpt = usersRepository.findById(userId);
        Optional<Parent> parentOpt = parentRepository.findByUser_Id(userId);

        if (userOpt.isEmpty() || parentOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        Users user = userOpt.get();
        Parent parent = parentOpt.get();

        Optional<Child> childOpt = childRepository.findFirstByParent_Id(parent.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("phone", parent.getPhone());

        childOpt.ifPresent(child -> {
            Long centerId = child.getVaccinationCenter().getId();
            if (centerId != null) {
                centerRepository
                    .findById(centerId)
                    .ifPresent(center -> {
                        response.put("vaccinationCenterId", center.getId());
                        response.put("vaccinationCenterName", center.getName());
                    });
            }
        });

        return ResponseEntity.ok(response);
    }

    //    // ✅ 3. جلب Parent ID عبر اسم المستخدم (لحل مشكلة React)
    //    @GetMapping("/parent-account/by-username")
    //    public ResponseEntity<?> getParentIdByUsername(@RequestParam String username) {
    //        Optional<Parent> parentOpt = parentRepository.findByUser_Username(username);
    //        if (parentOpt.isPresent()) {
    //            Map<String, Object> response = new HashMap<>();
    //            response.put("id", parentOpt.get().getId());
    //            return ResponseEntity.ok(response);
    //        } else {
    //            return ResponseEntity.status(404).body("Parent not found for username: " + username);
    //        }
    //    }

    // ✅ 2. تعديل بيانات الحساب
    @PutMapping("/parent-account")
    public ResponseEntity<?> updateAccount(@RequestBody Map<String, Object> request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Optional<Users> userOpt = usersRepository.findById(userId);
        Optional<Parent> parentOpt = parentRepository.findByUser_Id(userId);

        if (userOpt.isEmpty() || parentOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        Users user = userOpt.get();
        Parent parent = parentOpt.get();

        // ✅ تحديث رقم الهاتف
        String phone = (String) request.get("phone");
        if (phone != null && !phone.isBlank()) {
            parent.setPhone(phone);
            parentRepository.save(parent);
        }

        // ✅ تحديث كلمة المرور
        String password = (String) request.get("password");
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
            usersRepository.save(user);
        }

        return ResponseEntity.ok("تم تحديث البيانات بنجاح");
    }
}
