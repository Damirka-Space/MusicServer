package com.dam1rka.musicserver.controllers;

import com.dam1rka.musicserver.dtos.EntryMainDto;
import com.dam1rka.musicserver.services.FileService;
import com.dam1rka.musicserver.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestRestController {

    @Autowired
    private FileService fileService;

    @Autowired
    private TrackService trackService;


    @PostMapping(value = "/add")
    public ResponseEntity<?> test(@RequestBody EntryMainDto dto) {

        System.out.println(dto);

        return ResponseEntity.ok(dto);
    }

}
