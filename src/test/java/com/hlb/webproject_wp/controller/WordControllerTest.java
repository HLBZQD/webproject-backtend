package com.hlb.webproject_wp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlb.webproject_wp.entity.Word;
import com.hlb.webproject_wp.mapper.WordMapper;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:db/schema-h2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WordMapper wordMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Helper: create a word via POST /api/words and return its ID.
     */
    private Long createWord(String word, String translation) throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "word", word,
                "translation", translation,
                "difficultyLevel", 3
        ));

        String response = mockMvc.perform(post("/api/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("data").get("id").asLong();
    }

    @Test
    @Order(1)
    void listWords() throws Exception {
        mockMvc.perform(get("/api/words")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.data").isArray());
    }

    @Test
    @Order(2)
    void createWord() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "word", "hello",
                "translation", "你好",
                "difficultyLevel", 2,
                "phonetic", "həˈloʊ",
                "partOfSpeech", "interjection",
                "wordCategory", "greeting"
        ));

        mockMvc.perform(post("/api/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(notNullValue()))
                .andExpect(jsonPath("$.data.word").value("hello"))
                .andExpect(jsonPath("$.data.translation").value("你好"));
    }

    @Test
    @Order(3)
    void createWordValidationFailure() throws Exception {
        // Missing required fields: word and translation
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "difficultyLevel", 2
        ));

        mockMvc.perform(post("/api/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @Order(4)
    void getWordById() throws Exception {
        Long wordId = createWord("serendipity", "意外发现珍奇事物的本领");

        mockMvc.perform(get("/api/words/{id}", wordId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(wordId.intValue()))
                .andExpect(jsonPath("$.data.word").value("serendipity"))
                .andExpect(jsonPath("$.data.translation").value("意外发现珍奇事物的本领"));
    }

    @Test
    @Order(5)
    void getWordByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/words/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @Order(6)
    void updateWord() throws Exception {
        Long wordId = createWord("mountain", "山");

        String updateBody = objectMapper.writeValueAsString(Map.of(
                "word", "mountain",
                "translation", "山；山脉",
                "difficultyLevel", 4,
                "phonetic", "ˈmaʊntən"
        ));

        mockMvc.perform(put("/api/words/{id}", wordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(wordId.intValue()))
                .andExpect(jsonPath("$.data.translation").value("山；山脉"))
                .andExpect(jsonPath("$.data.difficultyLevel").value(4));
    }

    @Test
    @Order(7)
    void deleteWord() throws Exception {
        Long wordId = createWord("temporary", "临时的");

        mockMvc.perform(delete("/api/words/{id}", wordId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // Verify deleted
        mockMvc.perform(get("/api/words/{id}", wordId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @Order(8)
    void searchWords() throws Exception {
        insertWordDirectly("apple", "苹果");
        insertWordDirectly("banana", "香蕉");
        insertWordDirectly("application", "申请；应用");

        mockMvc.perform(get("/api/words")
                        .param("keyword", "app")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.data.length()").value(greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.data.data[0].word").value(org.hamcrest.Matchers.containsString("app")));
    }

    private void insertWordDirectly(String wordText, String translation) {
        Word word = new Word();
        word.setWord(wordText);
        word.setTranslation(translation);
        word.setDifficultyLevel(2);
        wordMapper.insert(word);
    }

    @Test
    @Order(9)
    void searchWordsNoMatch() throws Exception {
        mockMvc.perform(get("/api/words")
                        .param("keyword", "xyznonexistent999")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(0));
    }
}
