package com.dam1rka.musicserver.builders;

import com.dam1rka.musicserver.dtos.UserRegistrationDto;
import com.dam1rka.musicserver.entities.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Objects;

public class UserFactory {

    public static UserEntity fromUserDto(UserRegistrationDto user) {
        UserEntity newUser = new UserEntity();

        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        if(Objects.nonNull(user.getFirstname()))
            newUser.setFirstname(user.getFirstname());

        if(Objects.nonNull(user.getLastname()))
            newUser.setLastname(user.getLastname());

        Date created = new Date();

        newUser.setCreated(created);
        newUser.setUpdated(created);

        return newUser;
    }
}
