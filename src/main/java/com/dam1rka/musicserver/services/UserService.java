package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.builders.UserFactory;
import com.dam1rka.musicserver.dtos.UserRegistrationDto;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void registerUser(UserRegistrationDto user) {
        UserEntity newUser = UserFactory.fromUserDto(user);

        userRepository.save(newUser);

    }
}
