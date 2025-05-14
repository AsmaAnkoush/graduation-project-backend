package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import com.bzu.smartvax.repository.VaccinationRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VaccinationGeneratorService {

    private final VaccinationRepository vaccinationRepository;
    private final ScheduleVaccinationRepository scheduleVaccinationRepository;

    public VaccinationGeneratorService(
        VaccinationRepository vaccinationRepository,
        ScheduleVaccinationRepository scheduleVaccinationRepository
    ) {
        this.vaccinationRepository = vaccinationRepository;
        this.scheduleVaccinationRepository = scheduleVaccinationRepository;
    }

    public void generateVaccinationsForChild(Child child) {
        // ✅ جلب كل التطعيمات الأساسية من قاعدة البيانات
        List<Vaccination> defaultVaccines = vaccinationRepository.findAll();

        for (Vaccination vaccine : defaultVaccines) {
            // ✅ نأخذ targetAge بالأشهر ونحسب موعد التطعيم
            Integer targetAgeInMonths = vaccine.getTargetAge();

            if (targetAgeInMonths != null) {
                LocalDate scheduledDate = child.getDob().plusMonths(targetAgeInMonths);

                // ✅ إنشاء موعد مجدول
                ScheduleVaccination schedule = new ScheduleVaccination();
                schedule.setChild(child);
                schedule.setVaccination(vaccine);
                schedule.setScheduledDate(scheduledDate);
                schedule.setStatus("SCHEDULED");

                // ✅ حفظ في قاعدة البيانات
                scheduleVaccinationRepository.save(schedule);
            }
        }
    }
}
