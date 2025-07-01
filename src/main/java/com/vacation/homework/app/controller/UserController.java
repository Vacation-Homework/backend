package com.vacation.homework.app.controller;

import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.dto.*;
import com.vacation.homework.app.dto.base.DataResponseDto;
import com.vacation.homework.app.security.UserDetailsImpl;
import com.vacation.homework.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(summary = "아이디 중복검사", description = "중복된 아이디인지 검사합니다.")
    @ApiResponse(responseCode = "200", description = "중복됐는지 응답", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @GetMapping("/valid/id")
    public DataResponseDto<DuplicatedDto> isDuplicatedUserId(@RequestParam("userId") String userId) {
        return DataResponseDto.of(userService.isDuplicatedUserId(userId));
    }


    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @PostMapping
    public DataResponseDto<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUserId(), request.getPassword(), request.getNickname());;
        return DataResponseDto.empty();
    }


    @Operation(summary = "비밀번호 변경", description = "회원의 비밀번호를 변경합니다.")
    @PatchMapping("/password")
    public DataResponseDto<Void> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdatePasswordRequest request) {

        userService.updatePassword(userDetails.getUserSeq(), request.getNewPassword());
        return DataResponseDto.empty();
    }

    @Operation(summary = "회원정보 조회", description = "회원의 닉네임을 조회 합니다.")
    @GetMapping("/nickname")
    public DataResponseDto<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoResponse info = userService.getUserInfo(userDetails.getUserSeq());
        return DataResponseDto.of(info);
    }

    @Operation(summary = "닉네임 변경", description = "회원의 닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public DataResponseDto<Void> updateNickname(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateNicknameRequest request) {

        userService.updateNickname(userDetails.getUserSeq(), request.getNewNickname());
        return DataResponseDto.empty();
    }

    @Operation(summary = "로그아웃", description = "해당 사용자를 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public DataResponseDto<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userDetails.getUserSeq());
        return DataResponseDto.empty();
    }

    @Operation(summary = "회원 탈퇴", description = "해당 사용자를 탈퇴 처리합니다.")
    @DeleteMapping()
    public DataResponseDto<Void> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(userDetails.getUserSeq());
        return DataResponseDto.empty();
    }
}
