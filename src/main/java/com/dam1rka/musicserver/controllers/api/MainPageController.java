package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MainPageController {

    private final PageService mainService;

    public MainPageController(PageService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/main")
    public ResponseEntity<?> main(Principal principal) {
        try {
            return ResponseEntity.ok(mainService.loadMainBlocks());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
