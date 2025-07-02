package com.bzu.smartvax.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiVaccineValidationService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "AIzaSyAmHlQAljpXxnFnuAa1Rtr8pT8uargGM0I";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    /**
     * يتحقق إذا كان النص المدخل متعلق بالتطعيمات أو أعراض التطعيم أم لا.
     * @param text نص المستخدم
     * @return true إذا كان النص متعلق بالتطعيمات أو الأعراض بعد التطعيم، false خلاف ذلك
     */
    public boolean isVaccineRelated(String text) {
        String prompt =
            "هل النص التالي يتحدث عن التطعيمات أو الأعراض أو المشاكل الناتجة عن التطعيمات أو اللقاحات؟" +
            " أجب فقط بــ نعم أو لا، دون شرح. النص: " +
            text;

        Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(API_URL + "?key=" + API_KEY, request, Map.class);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
            String answer = parts.get(0).get("text").trim();

            // دعم بعض الإجابات المختلفة مثل "نعم." أو "نعم"
            return answer.startsWith("نعم");
        } catch (Exception e) {
            e.printStackTrace();
            return false; // إذا فشل التحقق، اعتبره ليس متعلق بالتطعيمات
        }
    }
}
