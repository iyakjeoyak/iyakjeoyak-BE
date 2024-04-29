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

    // 포인트를 삭제하기 애매해서 다른곳에서 적용 예정
    // 매 시간 정각에 실행
//    @Scheduled(cron = "0 0 * * * *")
//    public void cleanupExpiredPoint(){
//        pointHistoryService.cleanupExpiredPoint();
//    }
}
