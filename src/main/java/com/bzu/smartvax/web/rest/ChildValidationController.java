package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.CivilAffairsChild;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.CivilAffairsChildRepository;
import com.bzu.smartvax.service.dto.ChildValidationDTO;
import java.time.LocalDate;
import java.util.Optional;
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
        // ✅ 1. تحقق من رقم الهوية (يجب أن يكون 9 أرقام)
        if (!dto.getId().matches("\\d{9}")) {
            return ResponseEntity.badRequest().body("❌ رقم الهوية يجب أن يتكون من 9 أرقام.");
        }

        // ✅ 2. تحقق من رقم الهاتف (يجب أن يكون 10 أرقام)
        if (!dto.getPhone().matches("\\d{10}")) {
            return ResponseEntity.badRequest().body("📱 رقم الهاتف يجب أن يتكون من 10 أرقام.");
        }

        // ✅ 3. تحقق من تاريخ الميلاد
        LocalDate enteredDob;
        try {
            enteredDob = LocalDate.parse(dto.getDob());
            if (enteredDob.isAfter(LocalDate.now())) {
                return ResponseEntity.badRequest().body("📅 تاريخ الميلاد لا يمكن أن يكون في المستقبل.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ تنسيق تاريخ الميلاد غير صالح. استخدم yyyy-MM-dd.");
        }

        // ✅ 4. تحقق من وجود الطفل في سجل الشؤون المدنية
        Optional<CivilAffairsChild> civilRecordOpt = civilAffairsChildRepository.findById(dto.getId());
        if (civilRecordOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("🚫 الطفل غير موجود في السجل المدني.");
        }

        CivilAffairsChild record = civilRecordOpt.get();

        // ✅ 5. تحقق من تطابق الاسم
        if (!record.getName().equalsIgnoreCase(dto.getName())) {
            return ResponseEntity.badRequest().body("❌ اسم الطفل لا يطابق السجل المدني.");
        }

        // ✅ 6. تحقق من تطابق تاريخ الميلاد
        if (!record.getDob().equals(enteredDob)) {
            return ResponseEntity.badRequest().body("📅 تاريخ الميلاد لا يطابق السجل المدني.");
        }

        // ✅ 7. تحقق من تطابق رقم الهاتف
        if (!record.getPhone().equals(dto.getPhone())) {
            return ResponseEntity.badRequest().body("📞 رقم الهاتف لا يطابق السجل المدني.");
        }

        // ✅ 8. تحقق من أن الطفل غير مسجل مسبقًا في التطبيق
        if (childRepository.existsById(dto.getId())) {
            return ResponseEntity.status(409).body("⚠️ الطفل مسجل مسبقًا في التطبيق.");
        }

        // ✅ 9. كل شيء صحيح
        return ResponseEntity.ok("✅ تم التحقق بنجاح، يمكنك المتابعة.");
    }
}
