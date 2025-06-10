package com.vacation.homework.app.controller;

import com.vacation.homework.app.dto.TermsDto;
import com.vacation.homework.app.service.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    @Operation(summary = "최신 약관 조회", description = "현재 적용 중인 약관(version=app.terms.version)을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "약관 조회 성공")
    @GetMapping("/latest")
    public ResponseEntity<List<TermsDto>> getLatestTerms() {
        return ResponseEntity.ok(termsService.getLatestTerms());
    }
}