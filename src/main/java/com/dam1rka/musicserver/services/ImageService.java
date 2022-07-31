package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.ImageEnitiy;
import com.dam1rka.musicserver.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileService fileService;


    public byte[] getImage(Long id) {
        ImageEnitiy img = imageRepository.findById(id).orElse(null);

        if(Objects.isNull(img))
            throw new RuntimeException("Image not found");

        return fileService.loadImage(img.getUrl());
    }


}
