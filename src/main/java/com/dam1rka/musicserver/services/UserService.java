package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final WebClient webClient;

    private final LikeService likeService;

    @Value("${authserver}")
    private String authServer;

    private Optional<ResponseEntity<UserEntity>> getUser(String token) {
        return webClient.get()
                .uri(authServer + "/user/userinfo")
                .header("Authorization", token)
                .retrieve()
                .toEntity(UserEntity.class).blockOptional();
    }

    private UserEntity initUser(UserEntity user) {
        user = userRepository.save(user);

        likeService.initializeUser(user);

        return user;
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity getUserByToken(String token) throws UserPrincipalNotFoundException {
        var res = getUser(token);

        if(res.isPresent() && res.get().getStatusCode() == HttpStatus.OK) {
            if(Objects.nonNull(res.get().getBody()))
                return userRepository.findByUsername(res.get().getBody().getUsername());
            throw new UserPrincipalNotFoundException("User not found");
        }

        throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Can't get user info");
    }

    public UserEntity registerUser(String token) throws UserPrincipalNotFoundException {
        var res = getUser(token);

        if(res.isPresent() && res.get().getStatusCode() == HttpStatus.OK) {
            if(Objects.nonNull(res.get().getBody()))
                return initUser(res.get().getBody());
            throw new UserPrincipalNotFoundException("User not found");
        }

        throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Can't get user info");
    }

    public UserEntity checkUser(Principal principal) {
        if(Objects.nonNull(principal)) {
            return userRepository.findByUsername(principal.getName());
        }
        return null;
    }
}
