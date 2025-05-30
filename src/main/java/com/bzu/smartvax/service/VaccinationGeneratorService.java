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
        // ✅ جلب كل التطعيمات مع المجموعة المرتبطة فيها
        List<Vaccination> defaultVaccines = vaccinationRepository.findAll();

        for (Vaccination vaccine : defaultVaccines) {
            int days = vaccine.getGroup().getTargetAgeDays(); // ✅ من vaccination_group
            LocalDate scheduledDate = child.getDob().plusDays(days);

            ScheduleVaccination schedule = new ScheduleVaccination();
            schedule.setChild(child);
            schedule.setVaccination(vaccine);
            schedule.setScheduledDate(scheduledDate); // ✅ لا تغير الاسم
            schedule.setStatus("PENDING");
            schedule.setVaccinationGroup(vaccine.getGroup()); // ✅ السطر الجديد

            scheduleVaccinationRepository.save(schedule);
        }
    }
}
