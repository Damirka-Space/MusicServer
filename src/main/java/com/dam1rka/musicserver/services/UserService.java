package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.builders.UserFactory;
import com.dam1rka.musicserver.dtos.UserRegistrationDto;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity registerUser(UserRegistrationDto user) {
        UserEntity newUser = UserFactory.fromUserDto(user);

        return userRepository.save(newUser);
    }

    public UserEntity checkUser(Principal principal, HttpServletResponse response) {
        if(Objects.nonNull(principal)) {
            UserEntity user = userRepository.findByUsername(principal.getName());
            response.addHeader("Access-Control-Allow-Credentials", "true");
            return user;
        }
        return null;
    }
}
