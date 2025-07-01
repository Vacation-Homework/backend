package com.vacation.homework.app.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(nullable = false, length = 50)
    private String userId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 512)
    private String fcmToken;

    @Column(nullable = false, length = 5)
    private String nickname;

    @Column(length = 500)
    private String refreshToken;

    private LocalDateTime joinedAt;
    private LocalDateTime withdrawnAt;

    @Column(nullable = false)
    private Boolean isWithdrawn = false;

    // 약관 동의 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Agree> agreements;

    // 작성한 일기 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Homework> homeworks;
}
