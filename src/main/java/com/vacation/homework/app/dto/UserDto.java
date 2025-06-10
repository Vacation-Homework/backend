package com.vacation.homework.app.dto;


import com.vacation.homework.app.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {

    private Long userSeq;
    private String userId;
    private String nickname;
    private LocalDateTime joinedAt;
    private Boolean isWithdrawn;

    public static UserDto from(User user) {
        return UserDto.builder()
                .userSeq(user.getUserSeq())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .joinedAt(user.getJoinedAt())
                .isWithdrawn(user.getIsWithdrawn())
                .build();
    }
}
