package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduleVaccinationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ScheduleVaccination getScheduleVaccinationSample1() {
        return new ScheduleVaccination().id(1L).status("status1");
    }

    public static ScheduleVaccination getScheduleVaccinationSample2() {
        return new ScheduleVaccination().id(2L).status("status2");
    }

    public static ScheduleVaccination getScheduleVaccinationRandomSampleGenerator() {
        return new ScheduleVaccination().id(longCount.incrementAndGet()).status(UUID.randomUUID().toString());
    }
}
