package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    private AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAlbum(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(albumService.getAlbum(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tracks/get/{id}")
    public ResponseEntity<?> getTracks(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(albumService.getTracks(id));
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
            return ResponseEntity.ok(albumService.loadSmallImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value="/image/medium/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getMediumImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(albumService.loadMediumImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
