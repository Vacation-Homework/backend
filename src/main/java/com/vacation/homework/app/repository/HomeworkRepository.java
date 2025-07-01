package com.vacation.homework.app.repository;

import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    Optional<Homework> findByHomeworkSeqAndUser_userSeq(Long homeworkSeq, Long userSeq);

    Optional<Homework> findByHomeworkSeqAndUser_UserSeqAndIsDeletedFalse(Long homeworkSeq, Long userSeq);

    Optional<Homework> findByUserAndSelectedDate(User user, LocalDateTime selectedDate);

    List<Homework> findAllByUser_UserSeqAndIsDeletedFalseAndSelectedDateBetweenOrderBySelectedDateDesc(
            Long userSeq, LocalDateTime start, LocalDateTime end);
}