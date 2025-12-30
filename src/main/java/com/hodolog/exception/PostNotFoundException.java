package com.hodolog.exception;

public class PostNotFoundException extends RuntimeException {
    private PostNotFoundException(String message) {
        super(message);
    }

    public static PostNotFoundException create() {
        return new PostNotFoundException("게시글이 존재하지 않습니다.");
    }
}
