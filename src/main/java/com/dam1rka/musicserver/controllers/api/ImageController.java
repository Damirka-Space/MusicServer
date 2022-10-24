package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).body(imageService.getImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/small/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSmallImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).body(imageService.getSmailImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/medium/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getMediumImage(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).body(imageService.getMediumImage(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
