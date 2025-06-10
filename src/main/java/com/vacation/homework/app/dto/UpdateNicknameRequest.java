package com.vacation.homework.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "닉네임 변경 요청 DTO")
public class UpdateNicknameRequest {

    @Schema(description = "새 닉네임", example = "멋진개발자")
    private String newNickname;
}
