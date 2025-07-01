package com.vacation.homework.app.dto;


import com.vacation.homework.app.domain.Comment;
import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.domain.Weather;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HomeworkDto {

    private Long homeworkSeq;
    private String title;
    private String content;
    private Weather weather;
    private String photoUrl;
    private LocalDateTime selectedDate;
    private LocalDateTime createdAt;
    private String studentName;

    // 검수 결과 (optional)
    private String commentContent;
    private String spellCheckResult;

    public static HomeworkDto from(Homework hw, String studentName) {
        Comment comment = hw.getComment();

        return HomeworkDto.builder()
                .homeworkSeq(hw.getHomeworkSeq())
                .title(hw.getTitle())
                .content(hw.getContent())
                .weather(hw.getWeather())
                .photoUrl(hw.getPhotoUrl())
                .selectedDate(hw.getSelectedDate())
                .createdAt(hw.getBaseTime().getCreatedAt())
                .commentContent(comment != null ? comment.getContent() : null)
                .spellCheckResult(comment != null ? comment.getSpellCheckResult() : null)
                .studentName(studentName)
                .build();
    }

    public static HomeworkDto from(Homework hw) {
        Comment comment = hw.getComment();

        return HomeworkDto.builder()
                .homeworkSeq(hw.getHomeworkSeq())
                .title(hw.getTitle())
                .content(hw.getContent())
                .weather(hw.getWeather())
                .photoUrl(hw.getPhotoUrl())
                .selectedDate(hw.getSelectedDate())
                .createdAt(hw.getBaseTime().getCreatedAt())
                .commentContent(comment != null ? comment.getContent() : null)
                .spellCheckResult(comment != null ? comment.getSpellCheckResult() : null)
                .studentName(null)
                .build();
    }
}