package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Feedback getFeedbackSample1() {
        return new Feedback().id(1L).messageText("messageText1").sideEffects("sideEffects1").treatment("treatment1");
    }

    public static Feedback getFeedbackSample2() {
        return new Feedback().id(2L).messageText("messageText2").sideEffects("sideEffects2").treatment("treatment2");
    }

    public static Feedback getFeedbackRandomSampleGenerator() {
        return new Feedback()
            .id(longCount.incrementAndGet())
            .messageText(UUID.randomUUID().toString())
            .sideEffects(UUID.randomUUID().toString())
            .treatment(UUID.randomUUID().toString());
    }
}
