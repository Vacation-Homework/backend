package com.vacation.homework.app.service;

import com.vacation.homework.app.domain.User;
import com.vacation.homework.app.dto.LoginRequest;
import com.vacation.homework.app.dto.LoginResponse;
import com.vacation.homework.app.dto.UserDto;
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
            User user = userRepository.findById(userDetails.getUserSeq())
                    .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

            String accessToken = jwtUtil.createAccessToken(user);
            String refreshToken = jwtUtil.createRefreshToken(user);

            user.setRefreshToken(refreshToken);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    @Transactional
    public void logout(Long userSeq) {
        Optional<User> userOpt = userRepository.findByUserSeq(userSeq);
        userOpt.ifPresent(user -> user.setRefreshToken(null));
    }
}
