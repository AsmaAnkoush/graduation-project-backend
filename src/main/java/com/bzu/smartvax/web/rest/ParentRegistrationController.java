package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.*;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.CivilAffairsChildRepository;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.repository.UsersRepository;
import com.bzu.smartvax.service.VaccinationGeneratorService;
import com.bzu.smartvax.service.dto.ParentRegistrationDTO;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ParentRegistrationController {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final VaccinationGeneratorService vaccinationGeneratorService;
    private final CivilAffairsChildRepository civilAffairsChildRepository;

    public ParentRegistrationController(
        ParentRepository parentRepository,
        ChildRepository childRepository,
        UsersRepository usersRepository,
        PasswordEncoder passwordEncoder,
        VaccinationGeneratorService vaccinationGeneratorService,
        CivilAffairsChildRepository civilAffairsChildRepository
    ) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.vaccinationGeneratorService = vaccinationGeneratorService;
        this.civilAffairsChildRepository = civilAffairsChildRepository;
    }

    @PostMapping("/register-parent")
    public ResponseEntity<String> registerParent(@RequestBody ParentRegistrationDTO dto) {
        // ✅ التحقق من أن اسم المستخدم غير مستخدم
        if (usersRepository.existsByUsername(dto.username)) {
            return ResponseEntity.badRequest().body("❌ اسم المستخدم مستخدم من قبل.");
        }

        // ✅ التحقق من أن الطفل غير مسجل مسبقًا
        if (childRepository.existsById(dto.childId)) {
            return ResponseEntity.status(409).body("❌ الطفل مسجل مسبقًا في النظام");
        }

        // ✅ 1. إنشاء المستخدم وتشفير كلمة المرور
        Users user = new Users();
        user.setUsername(dto.username);
        user.setPassword(passwordEncoder.encode(dto.password));
        user.setRole("PARENT");
        usersRepository.save(user);

        // ✅ 2. إنشاء الأب وربطه بالمستخدم
        Parent parent = new Parent();
        parent.setName(dto.parentName);
        parent.setDob(LocalDate.parse(dto.parentDob));
        parent.setPhone(dto.parentPhone);
        parent.setRole("PARENT");
        parent.setUser(user);
        parentRepository.save(parent);

        user.setReferenceId(parent.getId()); // نفترض أن Users يحتوي على حقل referenceId من نوع Long
        usersRepository.save(user);

        // ✅ 3. إنشاء السجل الصحي الفارغ مؤقتًا
        HealthRecord healthRecord = new HealthRecord();
        healthRecord.setSensitivity(null);
        healthRecord.setDiabetes(null);
        healthRecord.setHighBloodPressure(null);
        healthRecord.setGeneticDiseases(null);
        healthRecord.setBloodType(null);

        // ✅ جلب سجل الطفل من الأحوال المدنية
        CivilAffairsChild civilRecord = civilAffairsChildRepository.findById(dto.childId).orElse(null);
        if (civilRecord == null) {
            return ResponseEntity.status(404).body("❌ الطفل غير موجود في السجل المدني");
        }

        Long centerId = civilRecord.getVaccinationCenter() != null ? civilRecord.getVaccinationCenter().getId() : null;

        // ✅ 4. إنشاء الطفل وربطه بالأب والسجل الصحي
        Child child = new Child();
        child.setId(dto.childId);
        child.setName(dto.childName);
        child.setDob(LocalDate.parse(dto.childDob));
        child.setParent(parent);
        child.setHealthRecord(healthRecord);

        if (centerId != null) {
            VaccinationCenter center = new VaccinationCenter();
            center.setId(centerId);
            child.setVaccinationCenter(center); // ✅ ربط الطفل باستخدام id فقط
        }

        childRepository.save(child);

        // ✅ 5. توليد مواعيد التطعيم تلقائيًا بناءً على تاريخ ميلاد الطفل
        vaccinationGeneratorService.generateVaccinationsForChild(child);

        return ResponseEntity.ok("✅ تم إنشاء الحساب بنجاح");
    }
}
