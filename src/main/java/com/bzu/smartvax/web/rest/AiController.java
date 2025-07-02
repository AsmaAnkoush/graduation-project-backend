package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.service.GeminiVaccineBotService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:3000")
public class AiController {

    @Autowired
    private GeminiVaccineBotService vaccineBotService;

    @PostMapping("/ask-vaccine-bot")
    public ResponseEntity<?> askVaccineBot(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "الرجاء إدخال سؤالك. السؤال لا يمكن أن يكون فارغاً."));
        }
        try {
            String answer = vaccineBotService.getVaccineBotAnswer(question);
            return ResponseEntity.ok(Map.of("answer", answer));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "حدث خطأ داخلي أثناء معالجة طلبك. يرجى المحاولة مرة أخرى لاحقًا."));
        }
    }
}
