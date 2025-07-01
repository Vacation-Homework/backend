package com.vacation.homework.app.rabbitMq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkCreatedMessage {
    private Long userSeq;
    private Long homeworkSeq;
}