package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.AlbumDto;
import com.dam1rka.musicserver.dtos.BlockDto;
import com.dam1rka.musicserver.dtos.PageDto;
import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.AlbumTypeEnum;
import com.dam1rka.musicserver.entities.BlockEntity;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.AlbumRepository;
import com.dam1rka.musicserver.repositories.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PageService {

    @Value("${file-server}")
    private String fileServer;

    private final BlockRepository blockRepository;
    private final AlbumRepository albumRepository;
    private final LikeService likeService;
    private final ListenHistoryService listenHistoryService;


    public PageDto loadMainBlocks(UserEntity user) {
        PageDto pageDto = new PageDto();
        List<BlockEntity> blockEntities = blockRepository.findAll();
        List<BlockDto> blocks = new LinkedList<>();

        // Recently listened block

        if(Objects.nonNull(user)) {
            List<AlbumEntity> albums =  listenHistoryService.getRecentlyListenedAlbums(user);

            if(albums.size() > 0) {
                BlockDto blockDto = new BlockDto();

                blockDto.setTitle("Вы недавно слушали");

                List<AlbumDto> albumDtos = new LinkedList<>();

                albums.forEach((album -> albumDtos.add(AlbumDto.fromAlbumEntity(album, fileServer))));

                blockDto.setAlbums(albumDtos);
                blocks.add(blockDto);
            }
        }

        for(BlockEntity block : blockEntities) {
            BlockDto blockDto = new BlockDto();

            blockDto.setTitle(block.getTitle());
            blockDto.setId(blockDto.getId());

            List<AlbumDto> albumDtos = new LinkedList<>();

            for(AlbumEntity album : block.getAlbums())
                albumDtos.add(AlbumDto.fromAlbumEntity(album, fileServer));

            blockDto.setAlbums(albumDtos);
            blocks.add(blockDto);
        }

        {
            BlockDto blockDto = new BlockDto();

            blockDto.setTitle("Новинки этого месяца");

            List<AlbumDto> albumDtos = new LinkedList<>();

            albumRepository.findAllOrderByCreated(PageRequest.of(0, 10)).forEach((album -> albumDtos.add(AlbumDto.fromAlbumEntity(album, fileServer))));

            blockDto.setAlbums(albumDtos);
            blocks.add(blockDto);
        }

        pageDto.setBlocks(blocks);

        return pageDto;
    }

    public PageDto loadCollectionBlocks(UserEntity user, AlbumTypeEnum albumType) {
        PageDto pageDto = new PageDto();

        List<BlockDto> blockDtos = new LinkedList<>();

        List<AlbumEntity> albums = likeService.getLikedByType(user, albumType);

        BlockDto blockDto = new BlockDto();

        List<AlbumDto> albumDtos = new LinkedList<>();

        albums.forEach(v -> albumDtos.add(AlbumDto.fromAlbumEntity(v, fileServer)));

        blockDto.setId(0L);

        switch (albumType) {
            case PLAYLIST -> blockDto.setTitle("Ваши плейлисты");
            case ALBUM -> blockDto.setTitle("Ваши альбомы");
        }

        blockDto.setAlbums(albumDtos);
        blockDtos.add(blockDto);
        pageDto.setBlocks(blockDtos);

        return pageDto;
    }

}
