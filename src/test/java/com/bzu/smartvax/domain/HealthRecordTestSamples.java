package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HealthRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HealthRecord getHealthRecordSample1() {
        return new HealthRecord().id(1L).sensitivity("sensitivity1").geneticDiseases("geneticDiseases1").bloodType("bloodType1");
    }

    public static HealthRecord getHealthRecordSample2() {
        return new HealthRecord().id(2L).sensitivity("sensitivity2").geneticDiseases("geneticDiseases2").bloodType("bloodType2");
    }

    public static HealthRecord getHealthRecordRandomSampleGenerator() {
        return new HealthRecord()
            .id(longCount.incrementAndGet())
            .sensitivity(UUID.randomUUID().toString())
            .geneticDiseases(UUID.randomUUID().toString())
            .bloodType(UUID.randomUUID().toString());
    }
}
