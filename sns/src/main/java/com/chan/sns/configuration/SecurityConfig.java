package com.chan.sns.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodedPassword() {
        return new BCryptPasswordEncoder();
    }
}
