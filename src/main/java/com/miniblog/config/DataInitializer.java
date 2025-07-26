package com.miniblog.config;

import com.miniblog.enums.Role;
import com.miniblog.model.User;
import com.miniblog.model.Post;
import com.miniblog.repository.UserRepository;
import com.miniblog.repository.PostRepository;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {Role role;
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@example.com") // <<< ЭТО
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            Post post = Post.builder()
                    .title("Добро пожаловать в Мини-Блог!")
                    .content("Это стартовый пост. Удалите его и создайте свои.")
                    .author(admin)
                    .createdAt(LocalDateTime.now())
                    .build();
            postRepository.save(post);
        }
    }
}
