package com.dam1rka.musicserver.dtos;

import com.dam1rka.musicserver.entities.AlbumEntity;
import lombok.Data;

@Data
public class AlbumDto {
    private Long id;
    private String title;
    private String description;
    private ImageDto image;

    public static AlbumDto fromAlbumEntity(AlbumEntity album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setTitle(album.getTitle());
        albumDto.setDescription(album.getDescription());

        ImageDto imageDto = new ImageDto();
        imageDto.setId(album.getImage().getId());

        albumDto.setImage(imageDto);

        return albumDto;
    }
}
