package com.dam1rka.musicserver.controllers.api;

import com.dam1rka.musicserver.services.FileUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadTestController {

    private final FileUploaderService fileUploaderService;

    @Autowired
    public UploadTestController(FileUploaderService fileUploaderService) {
        this.fileUploaderService = fileUploaderService;
    }




}
