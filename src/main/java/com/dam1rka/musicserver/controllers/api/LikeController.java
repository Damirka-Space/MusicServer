package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import com.dam1rka.musicserver.services.LikeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController()
@RequestMapping(value = "/like")
public class LikeController {

    private final UserRepository userRepository;
    private final LikeService likeService;

    @Autowired
    public LikeController(UserRepository userRepository, LikeService likeService) {
        this.userRepository = userRepository;
        this.likeService = likeService;
    }

    // Like/dislike album
    @GetMapping(value = "/album/{id}")
    public ResponseEntity<?> likeAlbum(@PathVariable long id, Principal principal, HttpServletResponse response) {
        if(Objects.nonNull(principal)) {
            UserEntity user = userRepository.findByUsername(principal.getName());
            response.addHeader("Access-Control-Allow-Credentials", "true");
            if(Objects.nonNull(user)) {
                likeService.likeAlbum(user, id);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.badRequest().body("You're not authorized");
    }

    // Like/dislike track
    @GetMapping(value = "/track/{id}")
    public ResponseEntity<?> likeTrack(@PathVariable long id, Principal principal, HttpServletResponse response) {
        if(Objects.nonNull(principal)) {
            UserEntity user = userRepository.findByUsername(principal.getName());
            response.addHeader("Access-Control-Allow-Credentials", "true");
            if(Objects.nonNull(user)) {
                likeService.likeTrack(user, id);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.badRequest().body("You're not authorized");
    }

}
