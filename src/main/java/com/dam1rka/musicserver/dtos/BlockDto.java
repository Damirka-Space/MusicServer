package com.dam1rka.musicserver.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BlockDto {
    private Long id;
    private String title;
    private List<AlbumDto> albums;

}
