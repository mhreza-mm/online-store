package com.store.onlinestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.onlinestore.dto.LoginRequest;
import com.store.onlinestore.entity.User;
import com.store.onlinestore.entity.User.Role;
import com.store.onlinestore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerDbTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        if (userRepository.findByUsername("dbuser").isEmpty()) {
            User user = new User();
            user.setUsername("dbuser");
            user.setPassword("1234");
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }

    @Test
    void loginWithCorrectPassword_ShouldReturnOk() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("dbuser");
        req.setPassword("1234");

        mockMvc.perform(
                        post("/api/auth/register-or-login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.massage").value("ورود موفقیت‌آمیز"))
                .andExpect(jsonPath("$.username").value("dbuser"))
                .andExpect(jsonPath("$.token").isNotEmpty());

    }

    @Test
    void loginWithWrongPassword_ShouldReturnUnauthorized() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("1234");
        req.setPassword("7225");

        mockMvc.perform(
                post("/api/auth/register-or-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)

        )

        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.massage").value("رمز عبور اشتباه است"));


    }
}
