package com.vacation.homework.app.controller;

import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.domain.Weather;
import com.vacation.homework.app.dto.CreateHomeworkRequest;
import com.vacation.homework.app.dto.HomeworkDto;
import com.vacation.homework.app.security.UserDetailsImpl;
import com.vacation.homework.app.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/homeworks")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "일기 저장", description = "새 일기를 작성하여 저장합니다.")
    @ApiResponse(responseCode = "200", description = "일기 저장 성공", content = @Content(schema = @Schema(implementation = HomeworkDto.class)))
    @PostMapping
    public ResponseEntity<HomeworkDto> createHomework(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateHomeworkRequest request) {
        homeworkService.saveHomework(
                userDetails.getUserSeq(),
                request.getTitle(),
                request.getContent(),
                request.getWeather(),
                request.getPhotoUrl()
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "일기 단건 조회", description = "homeworkSeq로 일기 상세정보를 조회합니다.")
    @GetMapping("/{homeworkSeq}")
    public ResponseEntity<HomeworkDto> getHomework(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long homeworkSeq) {
        Homework hw = homeworkService.getHomework(userDetails.getUserSeq(), homeworkSeq);
        return ResponseEntity.ok(HomeworkDto.from(hw));
    }


    @Operation(summary = "일기 월별 조회", description = "해당 유저의 특정 월 일기 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<HomeworkDto>> getHomeworksByMonth(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int year,
            @RequestParam int month) {

        List<HomeworkDto> list = homeworkService.getHomeworksByMonth(userDetails.getUserSeq(), year, month)
                .stream()
                .map(HomeworkDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "일기 삭제", description = "해당 일기를 소프트 삭제합니다.")
    @DeleteMapping("/{homeworkSeq}")
    public ResponseEntity<Void> deleteHomework(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long homeworkSeq) {
        homeworkService.deleteHomework(userDetails.getUserSeq(), homeworkSeq);
        return ResponseEntity.noContent().build();
    }
}
