package com.hlb.webproject_wp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:db/schema-h2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_USERNAME = "testuser_auth";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @Order(1)
    void registerSuccess() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "username", TEST_USERNAME,
                "password", TEST_PASSWORD,
                "email", TEST_EMAIL
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.data.id").value(notNullValue()));
    }

    @Test
    @Order(2)
    void registerDuplicateUsername() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "username", TEST_USERNAME,
                "password", TEST_PASSWORD,
                "email", TEST_EMAIL
        ));

        // First registration
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // Duplicate registration should return error code
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409));
    }

    @Test
    @Order(3)
    void registerMissingFields() throws Exception {
        // Empty body should trigger validation error
        String requestBody = objectMapper.writeValueAsString(Map.of());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @Order(4)
    void loginSuccess() throws Exception {
        // Register first
        String registerBody = objectMapper.writeValueAsString(Map.of(
                "username", "logintest",
                "password", "pass123456",
                "email", "login@test.com"
        ));
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        // Login
        String loginBody = objectMapper.writeValueAsString(Map.of(
                "username", "logintest",
                "password", "pass123456"
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value(notNullValue()))
                .andExpect(jsonPath("$.data.user").value(notNullValue()));
    }

    @Test
    @Order(5)
    void loginWrongPassword() throws Exception {
        // Register first
        String registerBody = objectMapper.writeValueAsString(Map.of(
                "username", "wrongpwtest",
                "password", "correct_password",
                "email", "wrong@test.com"
        ));
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        // Login with wrong password
        String loginBody = objectMapper.writeValueAsString(Map.of(
                "username", "wrongpwtest",
                "password", "wrong_password"
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }
}
