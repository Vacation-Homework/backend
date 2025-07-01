package com.vacation.homework.app.rabbitMq;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    private Long userSeq;
    private String title;
    private String body;
}