package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/list")
    public ResponseEntity<?> getGenres() {
        return ResponseEntity.ok(genreService.listGenres());
    }

}
