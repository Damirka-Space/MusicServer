package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.EntryMainDto;
import com.dam1rka.musicserver.entities.BlockEntity;
import com.dam1rka.musicserver.repositories.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainPageService {

    @Autowired
    private BlockRepository blockRepository;

    public EntryMainDto loadMainBlocks() {
        EntryMainDto entryMainDto = new EntryMainDto();
        List<BlockEntity> blocks = blockRepository.findAll();

        entryMainDto.setBlocks(blocks);

        return entryMainDto;
    }

}
