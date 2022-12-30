package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.dtos.TrackUploadDto;
import com.dam1rka.musicserver.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/track")
public class TrackController {

    private TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadFile(TrackUploadDto trackUploadDto) {
        try {
            trackService.saveTrack(trackUploadDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getTrack(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().contentType(MediaType.valueOf("audio/mpeg")).body(trackService.loadTrackById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
