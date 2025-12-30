package com.hodolog.controller;

import com.hodolog.domain.Post;
import com.hodolog.request.PostCreate;
import com.hodolog.response.ApiResponse;
import com.hodolog.response.PostResponse;
import com.hodolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    ApiResponse<String> get() {
        return ApiResponse.ok("Hello World");
    }

    @PostMapping("/posts")
    ApiResponse<PostResponse> post(@RequestBody @Valid PostCreate request) {
        PostResponse response = this.postService.write(request);
        return ApiResponse.ok(response);
    }
}
