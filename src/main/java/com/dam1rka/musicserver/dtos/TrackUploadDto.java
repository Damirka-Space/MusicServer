package com.dam1rka.musicserver.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TrackUploadDto {
    private String title;
    private String author;
    private String album;
    private MultipartFile image;
    private MultipartFile track;
}
