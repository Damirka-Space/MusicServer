package com.dam1rka.musicserver.dtos.channel;

import lombok.Data;

@Data
public class ChannelListDto {

    private Long id;

    private String title;

    private String description;

    private String ownerUsername;

    private Long userCount;
}
