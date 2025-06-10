package com.vacation.homework.app.service;


import com.vacation.homework.app.common.BaseTime;
import com.vacation.homework.app.domain.Agree;
import com.vacation.homework.app.domain.Terms;
import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.repository.AgreeRepository;
import com.vacation.homework.app.repository.TermsRepository;
import com.vacation.homework.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreeService {

    private final AgreeRepository agreeRepository;

    @Transactional(readOnly = true)
    public List<Agree> getAgreementsByUser(Long userSeq) {
        return agreeRepository.findByUser_UserSeq(userSeq);
    }
}
