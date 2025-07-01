package com.vacation.homework.app.service;

import com.vacation.homework.app.common.BaseTime;
import com.vacation.homework.app.domain.Agree;
import com.vacation.homework.app.domain.Terms;
import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.dto.DuplicatedDto;
import com.vacation.homework.app.dto.UserInfoResponse;
import com.vacation.homework.app.exception.Code;
import com.vacation.homework.app.exception.GeneralException;
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
        if (userRepository.findByUserIdAndIsWithdrawnFalse(userId).isPresent()) {
            throw new GeneralException(Code.DUPLICATE_USER_ID);
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

        userRepository.save(user);

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

        agreeRepository.saveAll(agrees);
    }

    @Transactional
    public void withdraw(Long userSeq) {
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        user.setIsWithdrawn(true);
        user.setWithdrawnAt(LocalDateTime.now());

        user.setFcmToken(null);
        user.setRefreshToken(null);
    }

    @Transactional
    public void updatePassword(Long userSeq, String newRawPassword) {
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        user.setPassword(passwordEncoder.encode(newRawPassword));
    }

    @Transactional
    public void updateNickname(Long userSeq, String newNickname) {
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        user.setNickname(newNickname);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userSeq) {
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));
        return UserInfoResponse.from(user);
    }

    @Transactional(readOnly = true)
    public DuplicatedDto isDuplicatedUserId(String userId) {
        Optional<User> user = userRepository.findByUserIdAndIsWithdrawnFalse(userId);
        boolean b= user.isEmpty();
        return DuplicatedDto.builder().duplicated(b).build();
    }

    @Transactional
    public void logout(Long userSeq) {
        User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));
        user.setFcmToken(null);
        user.setRefreshToken(null);
    }
}
