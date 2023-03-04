package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.entities.AlbumTypeEnum;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import com.dam1rka.musicserver.services.PageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;

@RestController()
@RequestMapping(value = "/collection")
public class CollectionPageController {

    private final UserRepository userRepository;
    private final PageService pageService;

    @Autowired
    public CollectionPageController(UserRepository userRepository, PageService pageService) {
        this.userRepository = userRepository;
        this.pageService = pageService;
    }

    @GetMapping(value = "/albums/")
    public ResponseEntity<?> getAlbums(Principal principal, HttpServletResponse response) {
        if(Objects.nonNull(principal)) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
            UserEntity user = userRepository.findByUsername(principal.getName());

            if(Objects.nonNull(user)) {
                return ResponseEntity.ok(pageService.loadCollectionBlocks(user, AlbumTypeEnum.ALBUM));
            }
        }
        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @GetMapping(value = "/playlists/")
    public ResponseEntity<?> getPlaylists(Principal principal, HttpServletResponse response) {
        if(Objects.nonNull(principal)) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
            UserEntity user = userRepository.findByUsername(principal.getName());

            if(Objects.nonNull(user)) {
                return ResponseEntity.ok(pageService.loadCollectionBlocks(user, AlbumTypeEnum.PLAYLIST));
            }
        }
        return ResponseEntity.badRequest().body("You're not authorized");
    }
}
