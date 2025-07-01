package com.vacation.homework.app.domain;


import com.vacation.homework.app.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "homework")
@NoArgsConstructor
@Builder
@Setter
@Getter
@AllArgsConstructor
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeworkSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Weather weather;

    private String photoUrl;

    @Column(nullable = false)
    private LocalDateTime selectedDate;

    @Embedded
    private BaseTime baseTime;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToOne(mappedBy = "homework", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Comment comment;
}