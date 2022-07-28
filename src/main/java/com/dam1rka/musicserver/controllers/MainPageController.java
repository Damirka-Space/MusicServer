package com.dam1rka.musicserver.controllers;

import com.dam1rka.musicserver.dtos.EntryMainDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class MainPageController {

    @GetMapping("/main")
    public EntryMainDto main(Principal principal) {

        return null;
    }
}
