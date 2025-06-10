package com.vacation.homework.app.security;

import com.vacation.homework.app.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long userSeq;
    private final String userId;
    private final String password;

    public UserDetailsImpl(User user) {
        this.userSeq = user.getUserSeq();
        this.userId = user.getUserId();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password; // DB에 암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return "";
    }

}