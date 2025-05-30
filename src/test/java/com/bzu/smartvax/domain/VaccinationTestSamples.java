package com.bzu.smartvax.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VaccinationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Vaccination getVaccinationSample1() {
        VaccineType vaccineType = new VaccineType();
        vaccineType.setId(6L);

        VaccinationGroup group = new VaccinationGroup();
        group.setId(1L);

        return new Vaccination()
            .id(1500L)
            .name("السل (الدرن)")
            .dose("0.05 مل")
            .treatment("أعلى الذراع الأيسر")
            .routeOfAdministration("في الجلد")
            .vaccineType(vaccineType)
            .group(group);
    }

    public static Vaccination getVaccinationSample2() {
        VaccineType vaccineType = new VaccineType();
        vaccineType.setId(8L);

        VaccinationGroup group = new VaccinationGroup();
        group.setId(1L);

        return new Vaccination()
            .id(1501L)
            .name("التهاب الكبد البائي (الجرعة الأولى)")
            .dose("0.5 مل")
            .treatment("عضلة الفخذ")
            .routeOfAdministration("في العضل")
            .vaccineType(vaccineType)
            .group(group);
    }

    public static Vaccination getVaccinationRandomSampleGenerator() {
        VaccineType vaccineType = new VaccineType();
        vaccineType.setId(longCount.incrementAndGet());

        VaccinationGroup group = new VaccinationGroup();
        group.setId(longCount.incrementAndGet());

        return new Vaccination()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .dose("0.5 مل")
            .treatment("عضلة الفخذ")
            .routeOfAdministration("في العضل")
            .vaccineType(vaccineType)
            .group(group);
    }
}
