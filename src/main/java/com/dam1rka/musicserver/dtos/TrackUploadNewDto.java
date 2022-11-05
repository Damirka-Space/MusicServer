package com.dam1rka.musicserver.dtos;

import lombok.Data;

@Data
public class TrackUploadNewDto {
    private String title;
    private String author;
    private byte[] track;
}
