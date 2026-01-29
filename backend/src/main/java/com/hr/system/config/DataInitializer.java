package com.hr.system.config;

import com.hr.system.entity.User;
import com.hr.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = userRepository.findByUsername("admin").orElse(new User());

        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123")); // Always reset to valid hash
        admin.setRole("HR");
        admin.setEnabled(true);

        userRepository.save(admin);
        System.out.println("------------------------------------------");
        System.out.println("ADMIN USER RESET SUCCESSFUL");
        System.out.println("Username: admin");
        System.out.println("Password: admin123");

    }
}
