package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChildTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Child getChildSample1() {
        return new Child().id(1L).name("name1");
    }

    public static Child getChildSample2() {
        return new Child().id(2L).name("name2");
    }

    public static Child getChildRandomSampleGenerator() {
        return new Child().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
