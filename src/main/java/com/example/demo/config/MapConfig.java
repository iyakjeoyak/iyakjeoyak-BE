package com.example.demo.config;

import com.example.demo.module.map.service.MapService;
import com.example.demo.module.map.service.MapServiceImpl;
import com.example.demo.module.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfig {
    @Value("${open_api.service_key.ermct}")
    private String serviceKey;
//
//    @Bean
//    public MapService mapService(MapServiceImpl mapServiceImpl) {
//        return mapService(mapServiceImpl(serviceKey));
//    }

    @Bean
    public MapServiceImpl mapServiceImpl(PharmacyRepository pharmacyRepository) {
        return new MapServiceImpl(serviceKey, pharmacyRepository);
    }
}
