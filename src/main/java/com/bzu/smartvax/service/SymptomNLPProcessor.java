package com.bzu.smartvax.service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class SymptomNLPProcessor {

    private static final Set<String> STOP_WORDS = Set.of("مع", "صار", "في", "كان", "من", "عن", "على", "انه", "الى", "ابني", "طفلي", "شوي");
    private static final Set<String> NEGATIONS = Set.of("ما", "فش", "ليس", "لا", "ابدا", "ولا", "بدون", "لم", "مش");

    private static Map<String, List<String>> SYMPTOM_SYNONYMS = new HashMap<>();

    // تحميل المرادفات عند تشغيل السيرفر
    @PostConstruct
    public void init() {
        loadSynonyms("symptoms.json");
    }

    // دالة: تحميل المرادفات من ملف JSON
    public void loadSynonyms(String filePath) {
        try (
            InputStream inputStream = new ClassPathResource(filePath).getInputStream();
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(reader);
            for (Object key : jsonObj.keySet()) {
                JSONArray arr = (JSONArray) jsonObj.get(key);
                List<String> values = new ArrayList<>();
                for (Object val : arr) values.add(normalizeWord(val.toString()));
                SYMPTOM_SYNONYMS.put(key.toString(), values);
            }
        } catch (Exception e) {
            System.err.println("خطأ في تحميل ملف المرادفات: " + e.getMessage());
        }
    }

    // دالة: إزالة التكرار الزائد في الحروف من كل كلمة (مثلاً: احمراررر => احمرار)
    public static String reduceRepeatedLetters(String word) {
        return word.replaceAll("(.)\\1{2,}", "$1"); // أي حرف مكرر أكثر من مرتين => مرة وحدة فقط
    }

    // دالة: تطبيع الحروف
    public static String normalizeWord(String word) {
        String result = word.trim();
        // توحيد الحروف
        result = result.replaceAll("[أإآا]", "ا");
        result = result.replaceAll("ى", "ي");
        result = result.replaceAll("ؤ", "و");
        result = result.replaceAll("ئ", "ي");
        result = result.replaceAll("ة", "ه");
        result = result.replaceAll("گ", "ك");
        // حذف التشكيل
        result = result.replaceAll("[\\u064B-\\u065F]", "");
        // إزالة الرموز والعلامات
        result = result.replaceAll("[،,:;\\.\\-\\_\\!\\؟\\*\\[\\]\\(\\)\\{\\}]", " ");
        result = result.replaceAll("\\s+", " ");
        // إزالة التكرار بالحروف
        result = reduceRepeatedLetters(result);
        return result.toLowerCase();
    }

    // دالة: تطبيع جملة كاملة وإزالة التكرار الزائد بالحروف
    public static String normalizeSentence(String text) {
        StringBuilder normalized = new StringBuilder();
        for (String word : text.split("\\s+")) {
            normalized.append(normalizeWord(word)).append(" ");
        }
        return normalized.toString().trim();
    }

    // الدالة الرئيسية لتحليل الأعراض
    public static Map<String, String> processSymptoms(String rawSymptomsInput) {
        String normalizedInput = normalizeSentence(rawSymptomsInput);

        // تقسيم لجمل فرعية للتعامل مع النفي
        String[] sentences = normalizedInput.split("[\\.!؟;،]");

        Map<String, String> symptomsMap = new HashMap<>();
        for (String symptom : SYMPTOM_SYNONYMS.keySet()) {
            boolean found = false;
            boolean negated = false;
            List<String> synonyms = SYMPTOM_SYNONYMS.get(symptom);

            for (String sentence : sentences) {
                List<String> tokens = new ArrayList<>(Arrays.asList(sentence.trim().split(" ")));
                tokens.removeIf(STOP_WORDS::contains);
                String sentenceStr = String.join(" ", tokens);

                // بحث عن أي مرادف (كلمة أو جملة)
                for (String synonym : synonyms) {
                    if (sentenceStr.contains(synonym)) {
                        found = true;
                        // فحص النفي في الجملة
                        for (String neg : NEGATIONS) {
                            if (
                                sentenceStr.contains(neg + " " + synonym) ||
                                sentenceStr.startsWith(neg + " ") ||
                                sentenceStr.contains(" " + neg + " ")
                            ) {
                                negated = true;
                                break;
                            }
                        }
                        // فحص أوسع: وجود النفي قبل المرادف
                        int idxSyn = sentenceStr.indexOf(synonym);
                        for (String neg : NEGATIONS) {
                            int idxNeg = sentenceStr.indexOf(neg);
                            if (idxNeg >= 0 && idxNeg < idxSyn) negated = true;
                        }
                    }
                    if (found) break;
                }
                if (found) break;
            }
            symptomsMap.put(symptom, (found && !negated) ? "yes" : "no");
        }
        return symptomsMap;
    }

    // REST Endpoint لتحليل الأعراض
    @PostMapping("/api/analyze-symptoms")
    public Map<String, String> analyzeSymptoms(@RequestBody Map<String, String> payload) {
        String symptomsText = payload.getOrDefault("text", "");
        return processSymptoms(symptomsText);
    }
}
