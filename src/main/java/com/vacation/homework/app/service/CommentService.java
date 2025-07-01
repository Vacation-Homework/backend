package com.vacation.homework.app.service;

import com.vacation.homework.app.domain.Comment;
import com.vacation.homework.app.domain.Homework;
import com.vacation.homework.app.exception.Code;
import com.vacation.homework.app.exception.GeneralException;
import com.vacation.homework.app.fcm.FcmService;
import com.vacation.homework.app.repository.CommentRepository;
import com.vacation.homework.app.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final HomeworkRepository homeworkRepository;
    private final CommentRepository commentRepository;
    private final FcmService fcmService;

    @Transactional
    public void saveComment(Long userSeq, Long homeworkSeq, String content, String spellCheckResult) {
        Homework homework = homeworkRepository.findByHomeworkSeqAndUser_userSeq(homeworkSeq, userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_HOMEWORK));

        commentRepository.save(Comment.builder()
                .homework(homework)
                .content(content)
                .spellCheckResult(spellCheckResult)
                .isDeleted(false)
                .build());

        String token = homework.getUser().getFcmToken();
        if (token != null) {
            fcmService.sendCommentNotification(token, homework.getTitle(), homework.getHomeworkSeq().toString());
        }
    }
}
