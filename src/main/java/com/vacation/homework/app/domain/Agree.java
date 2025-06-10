package com.vacation.homework.app.domain;


import com.vacation.homework.app.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agree")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agreeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_seq", nullable = false)
    private Terms terms;

    @Embedded
    private BaseTime baseTime;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}