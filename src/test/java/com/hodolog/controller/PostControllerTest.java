package com.hodolog.controller;

import com.hodolog.domain.Post;
import com.hodolog.exception.PostNotFoundException;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import com.hodolog.response.ApiCode;
import com.hodolog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        this.postRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /posts/{postId}: id값을 통해 게시글을 조회할 수 있습니다.")
    void getPosts_ok() throws Exception {
        // Given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        this.postRepository.save(post);

        // When & Then
        mockMvc.perform(get("/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("GET /posts/{postId}: 존재하지 않으면 404 + 정규화 에러 응답")
    void getPosts_notFound_404() throws Exception {
        // When & Then
        mockMvc.perform(get("/posts/{postId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ApiCode.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(PostNotFoundException.create().getMessage()))
                .andExpect(jsonPath("$.validation").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: code=OK, data에 title/content 포함")
    void postPosts_ok() throws Exception {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // When & Then
        performPost(this.toJson(postCreate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ApiCode.OK.getCode()))
                .andExpect(jsonPath("$.data.title").value(postCreate.getTitle()))
                .andExpect(jsonPath("$.data.content").value(postCreate.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: title이 비어있으면 VALIDATION_ERROR + validation.title")
    void postPosts_titleBlank_badRequest() throws Exception {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다")
                .build();

        // When & Then
        performPost(this.toJson(postCreate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ApiCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value("요청 값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.validation.title").value("제목은 필수값입니다."))
                .andExpect(jsonPath("$.validation.content").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: content가 비어있으면 VALIDATION_ERROR + validation.content")
    void postPosts_contentBlank_badRequest() throws Exception {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .build();

        // When & Then
        performPost(this.toJson(postCreate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ApiCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value("요청 값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.validation.content").value("내용은 필수값입니다."))
                .andExpect(jsonPath("$.validation.title").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("POST /posts: 요청시 DB에 값이 저장됩니다.")
    void postPosts_save_db() throws Exception {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        // When
        performPost(this.toJson(postCreate))
                .andExpect(status().isOk())
                .andDo(print());

        // Then
        assertEquals(1L, this.postRepository.count());

        Post post = this.postRepository.findAll().getFirst();

        assertEquals(postCreate.getTitle(), post.getTitle());
        assertEquals(postCreate.getContent(), post.getContent());
    }

    private ResultActions performPost(String json) throws Exception {
        return mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json));
    }


    private String toJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }
}
