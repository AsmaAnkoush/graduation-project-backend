package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppointmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Appointment getAppointmentSample1() {
        return new Appointment().id(1L).status("status1");
    }

    public static Appointment getAppointmentSample2() {
        return new Appointment().id(2L).status("status2");
    }

    public static Appointment getAppointmentRandomSampleGenerator() {
        return new Appointment().id(longCount.incrementAndGet()).status(UUID.randomUUID().toString());
    }
}
