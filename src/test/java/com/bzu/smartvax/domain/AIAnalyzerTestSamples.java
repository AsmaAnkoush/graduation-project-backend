package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AIAnalyzerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AIAnalyzer getAIAnalyzerSample1() {
        return new AIAnalyzer().id(1L).analysisResult("analysisResult1");
    }

    public static AIAnalyzer getAIAnalyzerSample2() {
        return new AIAnalyzer().id(2L).analysisResult("analysisResult2");
    }

    public static AIAnalyzer getAIAnalyzerRandomSampleGenerator() {
        return new AIAnalyzer().id(longCount.incrementAndGet()).analysisResult(UUID.randomUUID().toString());
    }
}
