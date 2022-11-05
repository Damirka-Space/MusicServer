package com.dam1rka.musicserver.dtos;

import lombok.Data;

@Data
public class AlbumUploadDto {
    private String title;
    private String author;
    private String genre;
    private String image;
    private String tracks;
}
