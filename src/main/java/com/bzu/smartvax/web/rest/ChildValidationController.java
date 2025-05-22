package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.CivilAffairsChild;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.CivilAffairsChildRepository;
import com.bzu.smartvax.service.dto.ChildValidationDTO;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChildValidationController {

    private final CivilAffairsChildRepository civilAffairsChildRepository;
    private final ChildRepository childRepository;

    public ChildValidationController(CivilAffairsChildRepository civilAffairsChildRepository, ChildRepository childRepository) {
        this.civilAffairsChildRepository = civilAffairsChildRepository;
        this.childRepository = childRepository;
    }

    @PostMapping("/validate-child-info")
    public ResponseEntity<String> validateChild(@RequestBody ChildValidationDTO dto) {
        if (!dto.getId().matches("\\d{9}")) {
            return ResponseEntity.badRequest().body("❌ رقم الهوية يجب أن يتكون من 9 أرقام.");
        }

        if (!dto.getPhone().matches("\\d{10}")) {
            return ResponseEntity.badRequest().body("📱 رقم الهاتف يجب أن يتكون من 10 أرقام.");
        }

        LocalDate enteredDob;
        try {
            enteredDob = LocalDate.parse(dto.getDob());
            if (enteredDob.isAfter(LocalDate.now())) {
                return ResponseEntity.badRequest().body("📅 تاريخ الميلاد لا يمكن أن يكون في المستقبل.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ تنسيق تاريخ الميلاد غير صالح. استخدم yyyy-MM-dd.");
        }

        CivilAffairsChild record;
        try {
            record = civilAffairsChildRepository.findById(dto.getId()).orElseThrow();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("🚫 الطفل غير موجود في السجل المدني.");
        }

        if (!record.getName().equalsIgnoreCase(dto.getName())) {
            return ResponseEntity.badRequest().body("❌ اسم الطفل لا يطابق السجل المدني.");
        }

        if (!record.getDob().equals(enteredDob)) {
            return ResponseEntity.badRequest().body("📅 تاريخ الميلاد لا يطابق السجل المدني.");
        }

        if (!record.getPhone().equals(dto.getPhone())) {
            return ResponseEntity.badRequest().body("📞 رقم الهاتف لا يطابق السجل المدني.");
        }

        if (childRepository.existsById(dto.getId())) {
            return ResponseEntity.status(409).body("⚠️ الطفل مسجل مسبقًا في التطبيق.");
        }

        return ResponseEntity.ok("✅ تم التحقق بنجاح، يمكنك المتابعة.");
    }
}
