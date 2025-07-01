package com.vacation.homework.app.service;

import com.vacation.homework.app.common.BaseTime;
import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.domain.Weather;
import com.vacation.homework.app.dto.HomeworkDto;
import com.vacation.homework.app.exception.Code;
import com.vacation.homework.app.rabbitMq.HomeworkMessageProducer;
import com.vacation.homework.app.repository.HomeworkRepository;
import com.vacation.homework.app.repository.UserRepository;
import com.vacation.homework.app.exception.GeneralException;
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
    private final HomeworkMessageProducer homeworkMessageProducer;

    @Transactional
    public Homework saveHomework(Long userSeq, LocalDateTime selectedDate, String title, String content, Weather weather, String photoUrl) {
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        homeworkRepository.findByUserAndSelectedDate(user, selectedDate)
                .ifPresent(h -> {
                    throw new GeneralException(Code.HOMEWORK_ALREADY_EXISTS);
                });

        Homework homework = homeworkRepository.save(Homework.builder()
                .user(user)
                .selectedDate(selectedDate)
                .title(title)
                .content(content)
                .weather(weather)
                .photoUrl(photoUrl)
                .baseTime(BaseTime.builder()
                        .createdAt(LocalDateTime.now())
                        .build())
                .isDeleted(false)
                .build());

        homeworkMessageProducer.sendHomeworkCreated(userSeq, homework.getHomeworkSeq());

        return homework;
    }

    @Transactional(readOnly = true)
    public HomeworkDto getHomework(Long userSeq, Long homeworkSeq) {
        Homework hw = homeworkRepository.findByHomeworkSeqAndUser_UserSeqAndIsDeletedFalse(homeworkSeq, userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_HOMEWORK_OR_UNAUTHORIZED));
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        return HomeworkDto.from(hw, user.getNickname());
    }

    @Transactional(readOnly = true)
    public List<Homework> getHomeworksByMonth(Long userSeq, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        return homeworkRepository.findAllByUser_UserSeqAndIsDeletedFalseAndSelectedDateBetweenOrderBySelectedDateDesc(
                userSeq, start, end);
    }

    @Transactional
    public void deleteHomework(Long userSeq, Long homeworkSeq) {
        Homework hw = homeworkRepository.findByHomeworkSeqAndUser_UserSeqAndIsDeletedFalse(homeworkSeq, userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_HOMEWORK_OR_UNAUTHORIZED));

        homeworkRepository.delete(hw);
    }
}
