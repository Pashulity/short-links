package com.pdidkovskiy.shortlinks.repository;

import domain.Role;
import domain.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserRepository implements Repository<String, User> {

    private static final Map<String, User> userStorage = new ConcurrentHashMap<>();

    static {
        userStorage.put("admin",new User("admin", Role.ROLE_ADMIN));
    }

    @Override
    public User save(User user) {
        return userStorage.computeIfAbsent(user.getLogin(), k -> user);
    }

    @Override
    public Optional<User> findOne(String login) {
        return Optional.ofNullable(userStorage.get(login));
    }

}
