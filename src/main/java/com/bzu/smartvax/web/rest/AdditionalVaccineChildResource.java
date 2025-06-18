package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.AdditionalVaccineChildService;
import com.bzu.smartvax.service.dto.AdditionalVaccineChildDTO;
import com.bzu.smartvax.service.dto.AdditionalVaccineDTO;
import com.bzu.smartvax.service.dto.ChildDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/additional-vaccine-child")
public class AdditionalVaccineChildResource {

    private final AdditionalVaccineChildService service;

    public AdditionalVaccineChildResource(AdditionalVaccineChildService service) {
        this.service = service;
    }

    // ✅ إنشاء سجل جديد عند تأكيد تطعيم من العامل الصحي
    @PostMapping
    public ResponseEntity<AdditionalVaccineChildDTO> create(@RequestBody AdditionalVaccineChildDTO dto) throws URISyntaxException {
        if (
            dto.getChild() == null ||
            dto.getChild().getId() == null ||
            dto.getAdditionalVaccine() == null ||
            dto.getAdditionalVaccine().getId() == null
        ) {
            return ResponseEntity.badRequest().build();
        }

        AdditionalVaccineChildDTO result = service.save(dto);
        return ResponseEntity.created(new URI("/api/additional-vaccine-child/" + result.getId())).body(result);
    }

    // ✅ جلب جميع السجلات
    @GetMapping
    public ResponseEntity<List<AdditionalVaccineChildDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // ✅ جلب سجل واحد بالمعرف
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalVaccineChildDTO> getOne(@PathVariable Long id) {
        Optional<AdditionalVaccineChildDTO> result = service.findOne(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // ✅ حذف سجل
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ جلب كل السجلات المرتبطة بطفل معين
    @GetMapping("/by-child/{childId}")
    public ResponseEntity<List<AdditionalVaccineChildDTO>> getByChildId(@PathVariable String childId) {
        return ResponseEntity.ok(service.findByChildId(childId));
    }

    // ✅ جلب كل السجلات المرتبطة بطفل وحالة معينة
    @GetMapping("/by-child/{childId}/status/{status}")
    public ResponseEntity<List<AdditionalVaccineChildDTO>> getByChildIdAndStatus(
        @PathVariable String childId,
        @PathVariable String status
    ) {
        return ResponseEntity.ok(service.findByChildIdAndStatus(childId, status));
    }
}
