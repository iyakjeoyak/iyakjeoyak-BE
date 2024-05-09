package com.example.demo.global.advice.aop;

import com.example.demo.global.advice.aop.annotation.MyLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MyLogAspect {
    @Around("@annotation(myLog)")
    public Object log(ProceedingJoinPoint joinPoint, MyLog myLog) throws Throwable {
        log.info("AOP 시작");
        long start = System.currentTimeMillis();
        try {
            log.info("실행 메서드 = {}", joinPoint.getSignature());
            return joinPoint.proceed();

        } catch (Exception e) {
            log.info("예외 발생 = {}", e.getMessage());
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            log.info("소요 시간 = {}ms", end - start);
            log.info("AOP 끝");
        }
    }
}
