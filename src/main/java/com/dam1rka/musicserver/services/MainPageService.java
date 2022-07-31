package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.EntryMainDto;
import com.dam1rka.musicserver.entities.BlockEntity;
import com.dam1rka.musicserver.repositories.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MainPageService {

    @Autowired
    private BlockRepository blockRepository;

    public EntryMainDto loadMainBlocks() {
        EntryMainDto entryMainDto = new EntryMainDto();
        BlockEntity block = blockRepository.findById(1L).orElse(null);

        entryMainDto.setBlocks(Collections.singletonList(block));

        return entryMainDto;
    }

}
