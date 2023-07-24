package com.salam.ftth.services;

import com.salam.ftth.db.entity.User;
import com.salam.ftth.repos.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    final UserRepository userRepository;


    public Optional<User> checkLogin(String username) {
        return userRepository.findUserByPrincipal(username, true);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
