package com.example.demo.config;

import com.example.demo.module.point.repository.PointHistoryRepository;
import com.example.demo.module.top_user.entity.TopUser;
import com.example.demo.module.top_user.repository.TopUserRepository;
import com.example.demo.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledTaskConfig {
    private final PointHistoryRepository pointHistoryRepository;
    private final TopUserRepository topUserRepository;
    
    // 일요일 00시 00분 00초 실행
    // 1주일간 포인트 가장 많이 모은 3명
    @Scheduled(cron = "0 0 0 * * 0")
    public void topPointUser(){
        LocalDateTime time = LocalDateTime.now().minusWeeks(1);
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int week = localDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        for (User user : pointHistoryRepository.findTop3UserAndSumChangedValue(time)) {
            TopUser topUser = topUserRepository.findByUserUserId(user.getUserId())
                    .orElse(TopUser.builder().user(user).year(year).week(week).build());
            topUserRepository.save(topUser);
        }
    }
}
