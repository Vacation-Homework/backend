package com.vacation.homework.app.dto;

import com.vacation.homework.app.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원정보 응답 DTO")
public class UserInfoResponse {
    @Schema(description = "닉네임", example = "멋진개발자")
    private String nickname;

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .nickname(user.getNickname())
                .build();
    }
}
