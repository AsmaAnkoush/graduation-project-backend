package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.ChildGrowthRecordService;
import com.bzu.smartvax.service.dto.ChildGrowthRecordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/growth-analysis")
public class ChildGrowthRecordController {

    private final ChildGrowthRecordService service;

    public ChildGrowthRecordController(ChildGrowthRecordService service) {
        this.service = service;
    }

    // ✅ تحليل وتخزين قياس جديد
    @PostMapping
    public ResponseEntity<String> analyzeGrowth(@RequestBody ChildGrowthRecordDTO dto) {
        String result = service.analyzeAndSave(dto);
        return ResponseEntity.ok(result);
    }

    // ✅ استرجاع كل القياسات السابقة للطفل
    @GetMapping("/{childId}")
    public ResponseEntity<?> getRecordsByChild(@PathVariable String childId) {
        return ResponseEntity.ok(service.getAllRecordsForChild(childId));
    }

    // ✅ Endpoint جديد يرجع القيم المرجعية حسب العمر بالأيام
    @GetMapping("/reference/{ageInDays}")
    public ResponseEntity<ChildGrowthRecordDTO> getReferenceByAge(@PathVariable int ageInDays) {
        ChildGrowthRecordDTO ref = service.getReferenceForAge(ageInDays);
        return ResponseEntity.ok(ref);
    }
}
