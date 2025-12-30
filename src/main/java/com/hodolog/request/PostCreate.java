package com.hodolog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Jackson이 JSON -> 객체로 만들 때 기본 생성자 필요
@AllArgsConstructor // 테스트/생성 편의
@Builder
public class PostCreate {
    @NotBlank(message = "제목은 필수값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수값입니다.")
    private String content;

}
