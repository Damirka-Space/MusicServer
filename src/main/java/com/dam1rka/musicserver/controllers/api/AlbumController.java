package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.dtos.AlbumUploadDto;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import com.dam1rka.musicserver.services.AlbumService;
import com.dam1rka.musicserver.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/album")
public class AlbumController {

    private AlbumService albumService;

    private UserService userService;

    @Autowired
    public AlbumController(AlbumService albumService, UserService userService) {
        this.albumService = albumService;
        this.userService = userService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAlbum(@PathVariable("id") Long id, Principal principal) {
        try {
            UserEntity user = null;
            if(Objects.nonNull(principal)) {
                user = userService.getUserByUsername(principal.getName());
            }
            return ResponseEntity.ok(albumService.getAlbum(user, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tracks/get/{id}")
    public ResponseEntity<?> getTracks(@PathVariable("id") Long id, Principal principal) {
        try {
            UserEntity user = null;
            if(Objects.nonNull(principal)) {
                user = userService.getUserByUsername(principal.getName());
            }
            return ResponseEntity.ok(albumService.getTracks(user, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/image/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(albumService.loadImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/image/small/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSmallImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).body(albumService.loadSmallImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/image/medium/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getMediumImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).body(albumService.loadMediumImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/upload/")
    public ResponseEntity<?> uploadAlbum(AlbumUploadDto albumUploadDto) {
        try {
            albumService.uploadAlbum(albumUploadDto);
            return ResponseEntity.ok("Album successful saved!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
