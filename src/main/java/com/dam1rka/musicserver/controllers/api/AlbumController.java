package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.dtos.TrackUploadDto;
import com.dam1rka.musicserver.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    private AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadAlbum(TrackUploadDto albumDto) {
//        try {
//            albumService.saveAlbum(albumDto);
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


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

    @GetMapping("/image/get/{id}")
    public ResponseEntity<?> getImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(albumService.loadImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
