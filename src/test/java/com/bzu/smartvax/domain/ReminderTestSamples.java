package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReminderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reminder getReminderSample1() {
        return new Reminder().id(1L).messageText("messageText1");
    }

    public static Reminder getReminderSample2() {
        return new Reminder().id(2L).messageText("messageText2");
    }

    public static Reminder getReminderRandomSampleGenerator() {
        return new Reminder().id(longCount.incrementAndGet()).messageText(UUID.randomUUID().toString());
    }
}
