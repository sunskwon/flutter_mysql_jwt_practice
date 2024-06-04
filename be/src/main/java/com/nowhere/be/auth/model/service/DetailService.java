package com.nowhere.be.auth.model.service;

import com.nowhere.be.auth.model.DetailsUser;
import com.nowhere.be.user.model.dto.LoginUserDTO;
import com.nowhere.be.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DetailService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public DetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUserDTO login = userService.findByUsername(username);

        if (Objects.isNull(login)) {
            throw new UsernameNotFoundException("해당하는 회원 정보가 존재하지 않습니다.");
        }

        return new DetailsUser(login);
    }
}
