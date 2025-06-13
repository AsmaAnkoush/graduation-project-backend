package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.domain.VaccinationCenter;
import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.repository.UsersRepository;
import com.bzu.smartvax.repository.VaccinationCenterRepository;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthWorkerAccountResource {

    private final UsersRepository usersRepository;
    private final HealthWorkerRepository healthWorkerRepository;
    private final VaccinationCenterRepository centerRepository;
    private final PasswordEncoder passwordEncoder;

    public HealthWorkerAccountResource(
        UsersRepository usersRepository,
        HealthWorkerRepository healthWorkerRepository,
        VaccinationCenterRepository centerRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usersRepository = usersRepository;
        this.healthWorkerRepository = healthWorkerRepository;
        this.centerRepository = centerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ 1. جلب بيانات الحساب
    @GetMapping("/health-worker-account")
    public ResponseEntity<?> getAccount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        Users user = userOpt.get();
        Optional<HealthWorker> workerOpt = healthWorkerRepository.findByUserReferenceId(user.getReferenceId());
        if (workerOpt.isEmpty()) return ResponseEntity.badRequest().body("Health worker not found");

        HealthWorker worker = workerOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("phone", worker.getPhone());

        if (worker.getVaccinationCenter() != null) {
            Long centerId = worker.getVaccinationCenter().getId();
            centerRepository
                .findById(centerId)
                .ifPresent(center -> {
                    response.put("vaccinationCenterId", center.getId());
                    response.put("vaccinationCenterName", center.getName());
                });
        }

        return ResponseEntity.ok(response);
    }

    // ✅ 2. تعديل بيانات الحساب
    @PutMapping("/health-worker-account")
    public ResponseEntity<?> updateAccount(@RequestBody Map<String, Object> request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        Users user = userOpt.get();
        Optional<HealthWorker> workerOpt = healthWorkerRepository.findByUserReferenceId(user.getReferenceId());
        if (workerOpt.isEmpty()) return ResponseEntity.badRequest().body("Health worker not found");

        HealthWorker worker = workerOpt.get();

        // ✅ تحديث رقم الهاتف
        String phone = (String) request.get("phone");
        if (phone != null && !phone.isBlank()) {
            worker.setPhone(phone);
            healthWorkerRepository.save(worker);
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
