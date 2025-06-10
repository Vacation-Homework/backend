package com.vacation.homework.app.repository;
import com.vacation.homework.app.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    List<Terms> findTermsByVersion(String s);
}
