package com.vacation.homework.app.controller;

import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.dto.CreateCommentRequest;
import com.vacation.homework.app.dto.CreateHomeworkRequest;
import com.vacation.homework.app.dto.HomeworkDto;
import com.vacation.homework.app.fcm.FcmService;
import com.vacation.homework.app.security.UserDetailsImpl;
import com.vacation.homework.app.service.CommentService;
import com.vacation.homework.app.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    @Value("${ai.secret-key}")
    private String aiSecretKey;

    private final CommentService commentService;
    private final HomeworkService homeworkService;

    @Operation(summary = "AI가 일기 조회", description = "AI가 일기 내용을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일기 조회 성공", content = @Content(schema = @Schema(implementation = HomeworkDto.class)))
    @GetMapping
    public ResponseEntity<HomeworkDto> getHomework(
            @RequestHeader("X-AI-KEY") String headerKey,
            @RequestParam("userSeq") Long userSeq,
            @RequestParam("homeworkSeq") Long homeworkSeq) {
        if (!headerKey.equals(aiSecretKey)) throw new IllegalArgumentException("AI 인증 키가 유효하지 않습니다.");
        HomeworkDto dto = homeworkService.getHomework(userSeq, homeworkSeq);
        return ResponseEntity.ok(dto);
    }



    @Operation(summary = "AI의 코멘트 저장", description = "AI의 코멘트를 저장합니다.")
    @ApiResponse(responseCode = "200", description = "AI코멘트 저장 성공", content = @Content(schema = @Schema(implementation = HomeworkDto.class)))
    @PostMapping
    public ResponseEntity<Void> createHomework(
            @RequestHeader("X-AI-KEY") String headerKey,
            @RequestBody CreateCommentRequest request) {
        if (!headerKey.equals(aiSecretKey)) throw new IllegalArgumentException("AI 인증 키가 유효하지 않습니다.");
        //코멘트 저장 및 알림전송
        commentService.saveComment(request.getUserSeq(), request.getHomeworkSeq(), request.getContent(), request.getSpellCheckResult());
        return ResponseEntity.noContent().build();
    }
}
