package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.AlbumDto;
import com.dam1rka.musicserver.dtos.BlockDto;
import com.dam1rka.musicserver.dtos.EntryMainDto;
import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.BlockEntity;
import com.dam1rka.musicserver.repositories.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MainPageService {

    @Autowired
    private BlockRepository blockRepository;

    public EntryMainDto loadMainBlocks() {
        EntryMainDto entryMainDto = new EntryMainDto();
        List<BlockEntity> blockEntities = blockRepository.findAll();
        List<BlockDto> blocks = new LinkedList<>();

        for(BlockEntity block : blockEntities) {
            BlockDto blockDto = new BlockDto();

            blockDto.setTitle(block.getTitle());
            blockDto.setId(blockDto.getId());

            List<AlbumDto> albumDtos = new LinkedList<>();

            for(AlbumEntity album : block.getAlbums())
                albumDtos.add(AlbumDto.fromAlbumEntity(album));

            blockDto.setAlbums(albumDtos);
            blocks.add(blockDto);
        }

        entryMainDto.setBlocks(blocks);

        return entryMainDto;
    }

}
