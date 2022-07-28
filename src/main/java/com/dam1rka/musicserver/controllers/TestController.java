package com.dam1rka.musicserver.controllers;

import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.TrackEntity;
import com.dam1rka.musicserver.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String main() {
        return "upload/upload";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String submit(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        fileService.saveFile(file);
        return "redirect:/test/";
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
