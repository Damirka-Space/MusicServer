package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.services.PageService;
import com.dam1rka.musicserver.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final PageService mainService;

    private final UserService userService;


    @GetMapping("/main")
    public ResponseEntity<?> main(Principal principal) {

        UserEntity user = userService.checkUser(principal);

        try {
            return ResponseEntity.ok(mainService.loadMainBlocks(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
