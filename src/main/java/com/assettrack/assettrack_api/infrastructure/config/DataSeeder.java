package com.assettrack.assettrack_api.infrastructure.config;

import com.assettrack.assettrack_api.infrastructure.persistence.entity.UserJpaEntity;
import com.assettrack.assettrack_api.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class DataSeeder {

    @Bean
    @Profile({"dev", "docker"})
    public CommandLineRunner seedUsers(UserJpaRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() > 0) return;

            userRepo.save(UserJpaEntity.builder()
                    .username("admin")
                    .password(encoder.encode("admin123456"))
                    .role("ROLE_ADMIN")
                    .active(true)
                    .build());

            userRepo.save(UserJpaEntity.builder()
                    .username("gestor")
                    .password(encoder.encode("gestor123456"))
                    .role("ROLE_GESTOR")
                    .active(true)
                    .build());

            userRepo.save(UserJpaEntity.builder()
                    .username("tecnico")
                    .password(encoder.encode("tec123456"))
                    .role("ROLE_TECNICO")
                    .active(true)
                    .build());

            System.out.println("Users created with success.");
        };
    }
}
