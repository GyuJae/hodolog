package com.hodolog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /posts: code=OK, data=Hello World")
    void getPosts_ok() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.data").value("Hello World"))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: code=OK, data에 title/content 포함")
    void postPosts_ok() throws Exception {
        performPost("""
                {"title":"제목입니다","content":"내용입니다"}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.title").value("제목입니다"))
                .andExpect(jsonPath("$.data.content").value("내용입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: title이 비어있으면 VALIDATION_ERROR + validation.title")
    void postPosts_titleBlank_badRequest() throws Exception {
        performPost("""
                {"title":"","content":"내용입니다"}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("요청 값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.validation.title").value("제목은 필수값입니다."))
                .andExpect(jsonPath("$.validation.content").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: content가 비어있으면 VALIDATION_ERROR + validation.content")
    void postPosts_contentBlank_badRequest() throws Exception {
        performPost("""
                {"title":"제목입니다","content":""}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("요청 값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.validation.content").value("내용은 필수값입니다."))
                .andExpect(jsonPath("$.validation.title").doesNotExist())
                .andDo(print());
    }

    private ResultActions performPost(String json) throws Exception {
        return mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json));
    }
}
