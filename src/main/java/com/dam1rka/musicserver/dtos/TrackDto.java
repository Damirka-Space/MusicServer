package com.dam1rka.musicserver.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TrackDto {

    private Long id;
    private String title;
    private Long[] authorId;
    private String[] author;
    private Long albumId;
    private String album;

}
