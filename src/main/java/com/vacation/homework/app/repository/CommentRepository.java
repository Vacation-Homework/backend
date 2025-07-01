package com.vacation.homework.app.repository;

import com.vacation.homework.app.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
