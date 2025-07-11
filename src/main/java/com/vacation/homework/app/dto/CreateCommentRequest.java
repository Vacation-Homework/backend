package com.vacation.homework.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Schema(description = "AI 코멘트 생성 요청 DTO")
public class CreateCommentRequest {
    private Long homeworkSeq;
    private Long userSeq;
    private String content;
    private String spellCheckResult;
}
