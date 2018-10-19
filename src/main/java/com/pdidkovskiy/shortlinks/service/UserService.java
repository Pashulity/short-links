package com.pdidkovskiy.shortlinks.service;

import com.pdidkovskiy.shortlinks.repository.InMemoryUserRepository;
import domain.Role;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private InMemoryUserRepository inMemoryUserRepository;

    public void login(String login) {
        User user = findUser(login).orElse(saveUser(login));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), null,
                AuthorityUtils.createAuthorityList(user.getRole().name()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Optional<User> findUser(String login) {
        return inMemoryUserRepository.findOne(login);
    }

    private User saveUser(String login) {
        User user = new User(login, Role.ROLE_USER);
        return inMemoryUserRepository.save(user);
    }

}
