package com.vacation.homework.app.repository;

import com.vacation.homework.app.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findAllByUser_UserSeqAndIsDeletedFalseAndBaseTime_CreatedAtBetween(
            Long userSeq,
            LocalDateTime start,
            LocalDateTime end
    );

    Optional<Homework> findByHomeworkSeqAndUser_UserSeqAndIsDeletedFalse(Long homeworkSeq, Long userSeq);

}