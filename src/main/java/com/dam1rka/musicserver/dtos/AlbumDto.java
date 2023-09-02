package com.dam1rka.musicserver.dtos;

import com.dam1rka.musicserver.entities.AlbumEntity;
import lombok.Data;

@Data
public class AlbumDto {
    private Long id;
    private String title;
    private String description;
    private ImageDto image;
    private String albumType;
    private String imageUrl;
    private boolean liked;

    public static AlbumDto fromAlbumEntity(AlbumEntity album, String baseUrl) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setTitle(album.getTitle());
        albumDto.setDescription(album.getDescription());
        albumDto.setAlbumType(album.getAlbumTypeEntity().getTitle());

        ImageDto imageDto = new ImageDto();
        imageDto.setId(album.getImage().getFileId());
        imageDto.setUrl(baseUrl + "mediumImages/" + imageDto.getId());

        albumDto.setImage(imageDto);

        albumDto.setLiked(false);

        return albumDto;
    }
}
