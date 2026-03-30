package com.ladi.stour.config;

import com.ladi.stour.entity.UserEntity;
import com.ladi.stour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.fullName}")
    private String adminFullName;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        // Check if admin user already exists
        if (userRepository.findByUsername(adminUsername).isPresent()) {
            log.info("Admin user already exists. Skipping initialization.");
            return;
        }

        try {
            UserEntity adminUser = UserEntity.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName(adminFullName)
                    .isActive(true)
                    .role("ADMIN")
                    .build();

            userRepository.save(adminUser);
            log.info("Admin user created successfully with username: {}", adminUsername);
        } catch (Exception e) {
            log.error("Error while initializing admin user", e);
        }
    }
}
