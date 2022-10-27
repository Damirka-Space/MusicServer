package com.dam1rka.musicserver.dtos;

import com.dam1rka.musicserver.entities.BlockEntity;
import lombok.Data;

import java.util.List;

@Data
public class EntryMainDto {

    List<BlockDto> blocks;

}
