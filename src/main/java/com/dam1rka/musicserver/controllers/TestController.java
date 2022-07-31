package com.dam1rka.musicserver.controllers;

import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.TrackEntity;
import com.dam1rka.musicserver.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    @ResponseBody
    public Principal test(Principal principal) {
        return principal;
    }

    @GetMapping("/file")
    public String filePage(Model model) {
        return "file";
    }

    @GetMapping("/tracks")
    @ResponseBody
    public TrackEntity tracks() {
        return null;
    }

    @GetMapping("/albums")
    @ResponseBody
    public AlbumEntity albums() {
        return null;
    }
}
