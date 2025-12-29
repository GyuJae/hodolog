package com.hodolog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @GetMapping("/posts")
    String get() {
        return "Hello World";
    }

    @PostMapping("/posts")
    String post(@RequestBody PostCreate request) {
        return request.getTitle();
    }
}
