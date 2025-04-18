package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VaccinationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vaccination getVaccinationSample1() {
        return new Vaccination()
            .id(1L)
            .name("name1")
            .type("type1")
            .sideEffects("sideEffects1")
            .targetAge(1)
            .status("status1")
            .treatment("treatment1");
    }

    public static Vaccination getVaccinationSample2() {
        return new Vaccination()
            .id(2L)
            .name("name2")
            .type("type2")
            .sideEffects("sideEffects2")
            .targetAge(2)
            .status("status2")
            .treatment("treatment2");
    }

    public static Vaccination getVaccinationRandomSampleGenerator() {
        return new Vaccination()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .sideEffects(UUID.randomUUID().toString())
            .targetAge(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .treatment(UUID.randomUUID().toString());
    }
}
