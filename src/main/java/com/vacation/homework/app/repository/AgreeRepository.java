package com.vacation.homework.app.repository;
import com.vacation.homework.app.domain.Agree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreeRepository extends JpaRepository<Agree, Long> {
    List<Agree> findByUser_UserSeq(Long userSeq);
}