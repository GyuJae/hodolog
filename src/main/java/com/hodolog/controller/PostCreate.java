package com.hodolog.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Jackson이 JSON -> 객체로 만들 때 기본 생성자 필요
@AllArgsConstructor // 테스트/생성 편의
public class PostCreate {
    private String title;
    private String content;
}
