package com.example.java_web_finalprj.config;

import com.example.java_web_finalprj.model.entity.User;
import com.example.java_web_finalprj.repository.UserRepository;
import com.example.java_web_finalprj.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountFixer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.findByUsername("admin").ifPresent(admin -> {
            admin.setPassword(HashUtil.hashPassword("123456"));
            userRepository.save(admin);
        });
    }
}