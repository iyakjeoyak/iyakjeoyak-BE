package com.example.demo.config;

import com.example.demo.global.exception.CustomException;
import com.example.demo.module.heart_medicine.repository.HeartMedicineRepository;
import com.example.demo.module.medicine.repository.MedicineRepository;
import com.example.demo.module.medicine_of_week.entity.MedicineOfWeek;
import com.example.demo.module.medicine_of_week.repository.MedicineOfWeekRepository;
import com.example.demo.module.point.repository.PointHistoryRepository;
import com.example.demo.module.review.repository.ReviewRepository;
import com.example.demo.module.top_user.entity.TopUser;
import com.example.demo.module.top_user.repository.TopUserRepository;
import com.example.demo.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.demo.global.exception.ErrorCode.MEDICINE_NOT_FOUND;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskConfig {
    private final PointHistoryRepository pointHistoryRepository;
    private final TopUserRepository topUserRepository;

    private final MedicineRepository medicineRepository;
    private final HeartMedicineRepository heartMedicineRepository;
    private final ReviewRepository reviewRepository;
    private final MedicineOfWeekRepository medicineOfWeekRepository;

    // 일요일 00시 00분 00초 실행
    // 1주일간 포인트 가장 많이 모은 3명
    @Scheduled(cron = "0 0 0 * * 0")
    public void topPointUser() {
        LocalDateTime time = LocalDateTime.now().minusWeeks(1);
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int week = localDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        int ranking = 1;
        for (User user : pointHistoryRepository.findTop3UserAndSumChangedValue(time)) {
            topUserRepository.save(TopUser.builder()
                    .user(user)
                    .year(year)
                    .week(week)
                    .ranking(ranking++)
                    .build());
        }
    }

    @Scheduled(cron = "0 0 0 * * 0")
    public void medicineOfWeek() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int week = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        HashMap<Long, Integer> hm = new HashMap<>();
        reviewRepository.findAllByCreatedDateBetween(lastWeek, now).forEach(r -> hm.put(r.getMedicine().getId(), hm.getOrDefault(r.getMedicine().getId(), 0) + 7));
        heartMedicineRepository.findAllByCreatedDateBetween(lastWeek, now).forEach(heart -> hm.put(heart.getMedicine().getId(), hm.getOrDefault(heart.getMedicine().getId(), 0) + 3));

        log.info(hm.toString());

        List<Long> medicineIds = new ArrayList<>(hm.keySet());
        medicineIds.sort((i1, i2) -> hm.get(i2).compareTo(hm.get(i1)));
        for (int i = 0; i < medicineIds.size(); i++) {
            if (i > 2) continue;
            MedicineOfWeek save = medicineOfWeekRepository.save(
                    MedicineOfWeek.builder()
                            .medicine(medicineRepository.findById(medicineIds.get(i)).orElseThrow(() -> new CustomException(MEDICINE_NOT_FOUND)))
                            .year(year)
                            .week(week)
                            .ranking(i + 1)
                            .build());
            log.info("{}년 {}주차 이주의 {}위 영양제 = {} / PK ={}", save.getYear(), save.getWeek(), save.getRanking(), save.getMedicine().getPRDLST_NM(), save.getId());
        }
    }
}
