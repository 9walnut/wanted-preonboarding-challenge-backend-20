package com.exception_study.global.config;

import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.*;

@TestConfiguration
public class TestSecurityConfig { // 테스트 환경을 분리하기위한 Config
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests)
                        -> authorizeHttpRequests
                        .anyRequest().permitAll()
                );
        return httpSecurity.build();
    }
}