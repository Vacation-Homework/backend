package com.vacation.homework.app.dto;

import com.vacation.homework.app.domain.Weather;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Schema(description = "일기 생성 요청 DTO")
public class CreateHomeworkRequest {
    private String title;
    private String content;
    private Weather weather;
    private String photoUrl;
}