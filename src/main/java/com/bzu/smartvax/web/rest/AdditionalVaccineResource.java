package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.AdditionalVaccineService;
import com.bzu.smartvax.service.dto.AdditionalVaccineDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/additional-vaccines")
public class AdditionalVaccineResource {

    private final AdditionalVaccineService vaccineService;

    public AdditionalVaccineResource(AdditionalVaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    // ✅ جلب كل اللقاحات الإضافية
    @GetMapping
    public ResponseEntity<List<AdditionalVaccineDTO>> getAll() {
        List<AdditionalVaccineDTO> list = vaccineService.findAll();
        return ResponseEntity.ok(list);
    }

    // ✅ جلب لقاح إضافي بواسطة ID
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalVaccineDTO> getById(@PathVariable Long id) {
        Optional<AdditionalVaccineDTO> dto = vaccineService.findById(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ إضافة لقاح جديد
    @PostMapping
    public ResponseEntity<AdditionalVaccineDTO> create(@RequestBody AdditionalVaccineDTO dto) {
        AdditionalVaccineDTO saved = vaccineService.create(dto);
        return ResponseEntity.ok(saved);
    }

    // ✅ تعديل لقاح موجود
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalVaccineDTO> update(@PathVariable Long id, @RequestBody AdditionalVaccineDTO dto) {
        Optional<AdditionalVaccineDTO> updated = vaccineService.update(id, dto);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ حذف لقاح
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = vaccineService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
