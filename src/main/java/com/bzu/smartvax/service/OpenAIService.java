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
public class OpenAIService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY =
        "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"; // 🔥 ضع مفتاح OpenAI هنا

    public String getSuggestions(String symptoms) {
        // نص البرومبت (السؤال الموجه للذكاء الاصطناعي)
        String prompt =
            "أنا طبيب أطفال. ظهرت الأعراض التالية على الطفل بعد التطعيم: " +
            symptoms +
            ". ماذا يجب أن أنصح به الأهل؟ قدم نصيحة طبية عملية وواضحة.";

        // إعداد Body للطلب
        Map<String, Object> body = Map.of(
            "model",
            "gpt-3.5-turbo", // يمكنك استخدام gpt-4 لو متاح بحسابك
            "messages",
            List.of(Map.of("role", "user", "content", prompt)),
            "temperature",
            0.7 // (تحكم في تنوع الإجابات)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY); // 🔥 هنا الفرق: Authorization عبر Bearer Token

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(API_URL, request, Map.class);

            // استخراج الإجابة من الرد
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ لم أتمكن من الحصول على نصيحة طبية من OpenAI.";
        }
    }
}
