package com.vacation.homework.app.domain;


import com.vacation.homework.app.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsSeq;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 10)
    private String version;

    @Embedded
    private BaseTime baseTime;

    @OneToMany(mappedBy = "terms", cascade = CascadeType.ALL)
    private List<Agree> agreements;
}