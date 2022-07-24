package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.UserRegistrationDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

//    private UserRepository userRepository;

//    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    private void checkUserDto(UserRegistrationDto user) {

    }

    public void registerUser(UserRegistrationDto user) {
        checkUserDto(user);

//        UserEntity newUser = UserFactory.fromUserDto(user);
//
//        userRepository.save(newUser);

    }

    public Object getUserByUsername(String name) {
        return null;
    }
}
