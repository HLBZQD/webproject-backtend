package com.hlb.webproject_wp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlb.webproject_wp.entity.User;
import com.hlb.webproject_wp.entity.Word;
import com.hlb.webproject_wp.mapper.UserMapper;
import com.hlb.webproject_wp.mapper.WordMapper;
import com.hlb.webproject_wp.security.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:db/schema-h2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PracticeRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a test user and return an auth token.
     */
    private String createUserAndGetToken(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setEmail(username + "@test.com");
        user.setRole("user");
        userMapper.insert(user);
        return "Bearer " + jwtUtil.generateToken(user.getId(), user.getUsername());
    }

    /**
     * Create a test word and return its ID.
     */
    private Long createWord(String wordText, String translation) {
        Word word = new Word();
        word.setWord(wordText);
        word.setTranslation(translation);
        word.setDifficultyLevel(2);
        wordMapper.insert(word);
        return word.getId();
    }

    @Test
    @Order(1)
    void submitRecord() throws Exception {
        // Setup: create user + word + token
        String token = createUserAndGetToken("practiceuser1");
        Long wordId = createWord("practice", "练习");

        String requestBody = objectMapper.writeValueAsString(Map.of(
                "wordId", wordId,
                "typingSpeedWpm", 45.5,
                "accuracy", 92.0,
                "durationSeconds", 120,
                "mistakesCount", 3,
                "completed", 1
        ));

        mockMvc.perform(post("/api/practice/records")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(notNullValue()))
                .andExpect(jsonPath("$.data.wordText").value("practice"))
                .andExpect(jsonPath("$.data.translation").value("练习"))
                .andExpect(jsonPath("$.data.typingSpeedWpm").value(45.5))
                .andExpect(jsonPath("$.data.accuracy").value(92.0));
    }

    @Test
    @Order(2)
    void submitRecordWithoutAuth() throws Exception {
        Long wordId = createWord("unauthorized", "未授权");

        String requestBody = objectMapper.writeValueAsString(Map.of(
                "wordId", wordId,
                "typingSpeedWpm", 30.0,
                "accuracy", 80.0,
                "durationSeconds", 60
        ));

        mockMvc.perform(post("/api/practice/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    @Order(3)
    void submitRecordWithInvalidWord() throws Exception {
        String token = createUserAndGetToken("practiceuser2");

        String requestBody = objectMapper.writeValueAsString(Map.of(
                "wordId", 99999,
                "typingSpeedWpm", 30.0,
                "accuracy", 80.0,
                "durationSeconds", 60
        ));

        mockMvc.perform(post("/api/practice/records")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @Order(4)
    void getUserRecords() throws Exception {
        // Setup: create user, word, and submit a record
        String token = createUserAndGetToken("practiceuser3");
        Long wordId = createWord("vocabulary", "词汇");

        // Get user ID from token for record submission
        // Submit a record first
        mockMvc.perform(post("/api/practice/records")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wordId", wordId,
                                "typingSpeedWpm", 50.0,
                                "accuracy", 95.0,
                                "durationSeconds", 180
                        ))))
                .andExpect(status().isOk());

        // Now we need the userId. Since @Transactional + MockMvc, we can't easily
        // extract userId from the token in this test. Use the userId from token decode.
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));

        // Get user records by userId param (bypasses SecurityContextUtil)
        mockMvc.perform(get("/api/practice/records")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.data[0].wordText").value("vocabulary"))
                .andExpect(jsonPath("$.data.data[0].translation").value("词汇"));
    }

    @Test
    @Order(5)
    void getUserStats() throws Exception {
        // Setup: create user, word, and submit 2 records
        String token = createUserAndGetToken("practiceuser4");
        Long wordId1 = createWord("statistics", "统计");
        Long wordId2 = createWord("analysis", "分析");

        // Submit 2 records
        mockMvc.perform(post("/api/practice/records")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wordId", wordId1,
                                "typingSpeedWpm", 60.0,
                                "accuracy", 90.0,
                                "durationSeconds", 100
                        ))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/practice/records")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wordId", wordId2,
                                "typingSpeedWpm", 40.0,
                                "accuracy", 80.0,
                                "durationSeconds", 200
                        ))))
                .andExpect(status().isOk());

        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));

        // Get stats
        mockMvc.perform(get("/api/practice/stats")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalPractices").value(2))
                .andExpect(jsonPath("$.data.totalDurationSeconds").value(300));
    }
}
