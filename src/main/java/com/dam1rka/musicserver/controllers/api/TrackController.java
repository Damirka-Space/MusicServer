package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.dtos.TrackUploadDto;
import com.dam1rka.musicserver.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/track")
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

//    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<?> getTrack(@PathVariable("id") Long id) {
//        try {
//            return ResponseEntity.ok(trackService.loadTrackById(id));
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping(value = "/get/{id}")
    public void getTrack(
            @PathVariable("id") Long id,
            HttpServletResponse response) {
        try {
            response.setContentType("audio/mpeg");
            InputStream is = trackService.loadStreamTrackById(id);
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
