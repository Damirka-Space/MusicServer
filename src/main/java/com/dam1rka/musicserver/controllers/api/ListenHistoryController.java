package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.services.ListenHistoryService;
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

@RestController
@RequestMapping("/history")
public class ListenHistoryController {

    private final UserService userService;
    private final ListenHistoryService listenHistoryService;

    @Autowired
    public ListenHistoryController(UserService userService, ListenHistoryService listenHistoryService) {
        this.userService = userService;
        this.listenHistoryService = listenHistoryService;
    }

    @GetMapping("/save/track/{id}")
    public ResponseEntity<?> save(@PathVariable("id") long id, Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user)) {
            listenHistoryService.saveToHistory(user, id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @GetMapping("/save/album/{id}")
    public ResponseEntity<?> saveAlbum(@PathVariable("id") long id, Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user)) {
            listenHistoryService.saveAlbumToHistory(user, id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @GetMapping("/show/all")
    public ResponseEntity<?> showAll(Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user))
            return ResponseEntity.ok(listenHistoryService.getHistory(user));

        return ResponseEntity.badRequest().body("You're not authorized");
    }

}
