package com.example.demo.global.memoryDb;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Repository
//@Service
@RequiredArgsConstructor
public class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        return stringStringValueOperations.get(key);
    }

    public void setData(String key, String value, Long duration) {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set(key, value, Duration.ofSeconds(duration));
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

}
