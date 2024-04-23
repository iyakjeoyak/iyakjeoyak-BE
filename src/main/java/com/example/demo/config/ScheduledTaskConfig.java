package com.example.demo.config;

import com.example.demo.module.point.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledTaskConfig {
    private final PointHistoryService pointHistoryService;

    // 매 시간 정각에 실행
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredPoint(){
        pointHistoryService.cleanupExpiredPoint();
    }
}
