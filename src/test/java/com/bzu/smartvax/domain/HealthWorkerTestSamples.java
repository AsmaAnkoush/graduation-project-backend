package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HealthWorkerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HealthWorker getHealthWorkerSample1() {
        return new HealthWorker()
            .id(1L)
            .username("username1")
            .password("password1")
            .phone("phone1")
            .age(1)
            .name("name1")
            .gender("gender1")
            .location("location1")
            .email("email1")
            .role("role1");
    }

    public static HealthWorker getHealthWorkerSample2() {
        return new HealthWorker()
            .id(2L)
            .username("username2")
            .password("password2")
            .phone("phone2")
            .age(2)
            .name("name2")
            .gender("gender2")
            .location("location2")
            .email("email2")
            .role("role2");
    }

    public static HealthWorker getHealthWorkerRandomSampleGenerator() {
        return new HealthWorker()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .age(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .role(UUID.randomUUID().toString());
    }
}
