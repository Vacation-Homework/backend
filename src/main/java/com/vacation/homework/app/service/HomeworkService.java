package com.vacation.homework.app.service;

import com.vacation.homework.app.common.BaseTime;
import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.domain.Weather;
import com.vacation.homework.app.repository.HomeworkRepository;
import com.vacation.homework.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveHomework(Long userSeq, String title, String content, Weather weather, String photoUrl) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Homework homework = Homework.builder()
                .user(user)
                .title(title)
                .content(content)
                .weather(weather)
                .photoUrl(photoUrl)
                .baseTime(BaseTime.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .isDeleted(false)
                .build();

        homeworkRepository.save(homework);
    }

    @Transactional(readOnly = true)
    public Homework getHomework(Long userSeq, Long homeworkSeq) {
        return homeworkRepository.findByHomeworkSeqAndUser_UserSeqAndIsDeletedFalse(homeworkSeq, userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 권한이 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Homework> getHomeworksByMonth(Long userSeq, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        return homeworkRepository.findAllByUser_UserSeqAndIsDeletedFalseAndBaseTime_CreatedAtBetween(
                userSeq, start, end);
    }

    @Transactional
    public void deleteHomework(Long userSeq, Long homeworkSeq) {
        Homework hw = homeworkRepository.findByHomeworkSeqAndUser_UserSeqAndIsDeletedFalse(homeworkSeq, userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 권한이 없습니다."));

        hw.setIsDeleted(true);
        hw.getBaseTime().setDeletedAt(LocalDateTime.now());
    }
}
