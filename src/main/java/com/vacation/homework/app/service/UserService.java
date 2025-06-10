package com.vacation.homework.app.service;

import com.vacation.homework.app.common.BaseTime;
import com.vacation.homework.app.domain.Agree;
import com.vacation.homework.app.domain.Terms;
import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.repository.AgreeRepository;
import com.vacation.homework.app.repository.TermsRepository;
import com.vacation.homework.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AgreeRepository agreeRepository;
    private final TermsRepository termsRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.terms.version}")
    private String termsVersion;

    public void register(String userId, String rawPassword, String nickname) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        List<Terms> termsList = termsRepository.findTermsByVersion(termsVersion);

        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(rawPassword))
                .nickname(nickname)
                .joinedAt(now)
                .isWithdrawn(false)
                .build();

        // 1. 유저 먼저 저장 (PK 생성)
        userRepository.save(user);

        // 2. 동의 객체들 생성
        List<Agree> agrees = termsList.stream()
                .map(terms -> Agree.builder()
                        .user(user)
                        .terms(terms)
                        .baseTime(BaseTime.builder()
                                .createdAt(now)
                                .build())
                        .isDeleted(false)
                        .build())
                .toList();

        // 3. 일괄 저장
        agreeRepository.saveAll(agrees);
    }

    @Transactional(readOnly = true)
    public Optional<User> getByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public void withdraw(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.setIsWithdrawn(true);
        user.setWithdrawnAt(LocalDateTime.now());
    }

    @Transactional
    public void updatePassword(Long userSeq, String newRawPassword) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.setPassword(passwordEncoder.encode(newRawPassword));
    }

    @Transactional
    public void updateNickname(Long userSeq, String newNickname) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.setNickname(newNickname);
    }
}
