package com.hodolog.service;

import com.hodolog.domain.Post;
import com.hodolog.exception.PostNotFoundException;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import com.hodolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        this.postRepository.save(post);

        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public PostResponse get(Long postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::create);

        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
