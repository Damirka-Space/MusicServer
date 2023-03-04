package com.dam1rka.musicserver.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TrackDto {

    private Long id;
    private String title;
    private List<Long> authorId;
    private List<String> author;
    private Long albumId;
    private String album;
    private String url;
    private String imageUrl;
    private String metadataImageUrl;
    private boolean liked;

}
