package com.vacation.homework.app.domain;


import com.vacation.homework.app.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_seq", nullable = false)
    private Homework homework;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 1000)
    private String spellCheckResult;

    @Embedded
    private BaseTime baseTime;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}