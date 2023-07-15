package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.services.LikeService;
import com.dam1rka.musicserver.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;

@RestController()
@RequestMapping(value = "/like")
public class LikeController {

    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public LikeController(UserService userService, LikeService likeService) {
        this.userService = userService;
        this.likeService = likeService;
    }

    // Like/dislike album
    @GetMapping(value = "/album/{id}")
    public ResponseEntity<?> likeAlbum(@PathVariable long id, Principal principal) {
        UserEntity user = userService.checkUser(principal);
        if(Objects.nonNull(user)) {
            likeService.likeAlbum(user, id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("You're not authorized");
    }

    // Like/dislike track
    @GetMapping(value = "/track/{id}")
    public ResponseEntity<?> likeTrack(@PathVariable long id, Principal principal) {
        UserEntity user = userService.checkUser(principal);
        if(Objects.nonNull(user)) {
            likeService.likeTrack(user, id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("You're not authorized");
    }

}
