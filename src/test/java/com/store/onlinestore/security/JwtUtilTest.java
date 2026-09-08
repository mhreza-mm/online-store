package com.store.onlinestore.security;

import com.store.onlinestore.entity.Role;
import com.store.onlinestore.entity.User;
import com.store.onlinestore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JwtUtilDatabaseTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("1234  ");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole(User.Role.USER);

        savedUser = userRepository.save(user);
    }

    @Test
    void generateAndValidateToken_ShouldWorkWithDatabaseUser() {
        String token = jwtUtil.generateJwtToken(savedUser.getUsername());

        assertThat(jwtUtil.extractUsername(token)).isEqualTo(savedUser.getUsername());
        assertThat(jwtUtil.validateToken(token, savedUser.getUsername())).isTrue();
    }
}
