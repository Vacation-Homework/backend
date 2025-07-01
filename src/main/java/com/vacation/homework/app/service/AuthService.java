package com.vacation.homework.app.service;

import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.dto.LoginRequest;
import com.vacation.homework.app.dto.LoginResponse;
import com.vacation.homework.app.exception.Code;
import com.vacation.homework.app.exception.GeneralException;
import com.vacation.homework.app.repository.UserRepository;
import com.vacation.homework.app.security.UserDetailsImpl;
import com.vacation.homework.app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword())
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findByUserSeqAndIsWithdrawnFalse(userDetails.getUserSeq())
                    .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

            String accessToken = jwtUtil.createAccessToken(user);
            String refreshToken = jwtUtil.createRefreshToken(user);

            user.setRefreshToken(refreshToken);
            user.setFcmToken(request.getFcmToken());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new GeneralException(Code.INVALID_CREDENTIALS);
        }
    }

    @Transactional
    public void logout(Long userSeq) {
        Optional<User> userOpt = userRepository.findByUserSeq(userSeq);
        userOpt.ifPresent(user -> user.setRefreshToken(null));
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken))
            throw new GeneralException(Code.INVALID_REFRESH_TOKEN);

        String userSeq = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findByUserIdAndIsWithdrawnFalse(userSeq)
                .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        if (!refreshToken.equals(user.getRefreshToken()))
            throw new GeneralException(Code.REFRESH_TOKEN_MISMATCH);

        String newAccessToken = jwtUtil.createAccessToken(user);

        Date expiration = jwtUtil.getExpiration(refreshToken);
        Duration remaining = Duration.between(Instant.now(), expiration.toInstant());

        String newRefreshToken = refreshToken;

        if (remaining.compareTo(Duration.ofDays(3)) <= 0) {
            newRefreshToken = jwtUtil.createRefreshToken(user);
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);
        }

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

}
