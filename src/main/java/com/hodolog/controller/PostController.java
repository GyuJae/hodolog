package com.hodolog.controller;

import com.hodolog.controller.request.PostCreate;
import com.hodolog.controller.response.ApiResponse;
import com.hodolog.controller.response.PostResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @GetMapping("/posts")
    ApiResponse<String> get() {
        return ApiResponse.ok("Hello World");
    }

    @PostMapping("/posts")
    ApiResponse<PostResponse> post(@RequestBody @Valid PostCreate request) {
        PostResponse response = PostResponse.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return ApiResponse.ok(response);
    }
}
