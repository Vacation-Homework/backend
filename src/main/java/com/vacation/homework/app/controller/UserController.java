package com.vacation.homework.app.controller;

import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.dto.RegisterRequest;
import com.vacation.homework.app.dto.UpdateNicknameRequest;
import com.vacation.homework.app.dto.UpdatePasswordRequest;
import com.vacation.homework.app.dto.UserDto;
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

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUserId(), request.getPassword(), request.getNickname());;
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "비밀번호 변경", description = "회원의 비밀번호를 변경합니다.")
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdatePasswordRequest request) {

        userService.updatePassword(userDetails.getUserSeq(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "닉네임 변경", description = "회원의 닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateNicknameRequest request) {

        userService.updateNickname(userDetails.getUserSeq(), request.getNewNickname());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 탈퇴", description = "해당 사용자를 탈퇴 처리합니다.")
    @DeleteMapping()
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(userDetails.getUserSeq());
        return ResponseEntity.noContent().build();
    }
}
