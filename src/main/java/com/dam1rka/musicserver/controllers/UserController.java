package com.dam1rka.musicserver.controllers;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${website}")
    private String website;

    @GetMapping(value = "/login")
    public void user(HttpServletResponse response) throws IOException {
        response.sendRedirect(website);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<?> user(Principal principal, @RequestHeader(name="Authorization") String token) {
        if(Objects.nonNull(principal)) {
            UserEntity user = userService.getUserByUsername(principal.getName());
            if(Objects.isNull(user)) {
                // register new user from auth/user/userinfo
                try {
                    user = userService.registerUser(token);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }


}
