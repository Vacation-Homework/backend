package com.vacation.homework.app.controller;

import com.vacation.homework.app.dto.LoginRequest;
import com.vacation.homework.app.dto.LoginResponse;
import com.vacation.homework.app.dto.base.DataResponseDto;
import com.vacation.homework.app.security.UserDetailsImpl;
import com.vacation.homework.app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public DataResponseDto<String> login() {
        return DataResponseDto.of("안녕하세요!");
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하고 JWT를 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않음")
    })
    @PostMapping("/login")
    public DataResponseDto<LoginResponse> login(@RequestBody LoginRequest request) {
        return DataResponseDto.of(authService.login(request));
    }

    @Operation(summary = "로그아웃", description = "서버에 저장된 RefreshToken을 제거합니다.")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    @PostMapping("/logout")
    public DataResponseDto<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(userDetails.getUserSeq());
        return DataResponseDto.empty();
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 통해 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "리프레시 토큰이 유효하지 않음")
    })
    @PostMapping("/refresh")
    public DataResponseDto<LoginResponse> refresh(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = authHeader.replace("Bearer ", "");
        return DataResponseDto.of(authService.refreshToken(refreshToken));
    }

}
