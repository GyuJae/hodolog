package com.hodolog.service;

import com.hodolog.domain.Post;
import com.hodolog.exception.PostNotFoundException;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import com.hodolog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clear() {
        this.postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글을 생성할 수 있습니다.")
    void write() {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // When
        this.postService.write(postCreate);

        // Then
        assertEquals(1L, this.postRepository.count());

        Post post = this.postRepository.findAll().getFirst();
        assertEquals(postCreate.getTitle(), post.getTitle());
        assertEquals(postCreate.getContent(), post.getContent());
    }

    @Test
    @DisplayName("id값을 통해 게시글 하나를 가져올 수 있습니다.")
    void get() {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        this.postService.write(postCreate);

        Post post = this.postRepository.findAll().getFirst();

        // When
        PostResponse response = this.postService.get(post.getId());

        // When
        assertEquals(response.getTitle(), post.getTitle());
        assertEquals(response.getContent(), post.getContent());
    }

    @Test
    @DisplayName("id값을 통해 존재하지 않는 게시글 조회 시 예외를 던집니다.")
    void not_found_get() {
        // When & Then
        assertThrows(PostNotFoundException.class, () -> this.postService.get(1L));
    }
}