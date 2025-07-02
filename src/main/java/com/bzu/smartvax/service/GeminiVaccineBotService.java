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
public class GeminiVaccineBotService { // <--- اسم الكلاس الجديد

    @Autowired
    private RestTemplate restTemplate;

    // TODO: يفضل وضع هذا المفتاح في ملف application.properties/yml كمتغير بيئة لأمان أفضل
    // مثال: gemini.api.key=AIzaSy...
    private static final String API_KEY = "AIzaSyAmHlQAljpXxnFnuAa1Rtr8pT8uargGM0I"; // استخدم مفتاحك الخاص هنا
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    /**
     * يولد إجابة من Gemini AI حول استفسار متعلق بالتطعيمات.
     * @param question سؤال المستخدم
     * @return الإجابة التي يولدها Gemini AI
     */
    public String getVaccineBotAnswer(String question) {
        // بناء الـ prompt لـ Gemini AI
        // قمنا بتوجيه Gemini ليكون متخصصًا في التطعيمات فقط
        String prompt =
            "أنت مساعد افتراضي متخصص في الإجابة عن كل الاستفسارات حول التطعيمات، " +
            "بما في ذلك أهميتها، أنواعها، جداولها الموصى بها، وآثارها الجانبية. " +
            "اجب على السؤال التالي بوضوح وإيجاز، وفي حال كان السؤال خارج نطاق التطعيمات، " +
            "اذكر أنك مختص فقط في معلومات التطعيمات. السؤال: " +
            question;

        // بناء جسم الطلب (Request Body) لواجهة Gemini API
        Map<String, Object> body = Map.of(
            "contents",
            List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
            "generationConfig",
            Map.of(
                // يمكن إضافة إعدادات إضافية للتحكم في الإجابة
                "temperature",
                0.7 // للتحكم في مدى إبداع الإجابة (0.0 للمزيد من الدقة)
            )
        );

        // إعداد الهيدرز (Headers) للطلب
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // بناء الكيان (HttpEntity) الذي يحتوي على الجسم والهيدرز
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // إرسال الطلب واستقبال الاستجابة
            Map<String, Object> response = restTemplate.postForObject(API_URL + "?key=" + API_KEY, request, Map.class);

            // استخراج الإجابة من استجابة Gemini
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).get("text").trim();
                }
            }
            return "لم يتمكن لقاح بوت من إيجاد إجابة."; // fallback if no answer part found
        } catch (Exception e) {
            e.printStackTrace();
            return "حدث خطأ أثناء التواصل مع لقاح بوت. يرجى المحاولة مرة أخرى لاحقًا.";
        }
    }
}
