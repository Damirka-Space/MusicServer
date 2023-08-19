package com.dam1rka.musicserver.dtos.channel;

import lombok.Data;

import java.util.Date;

@Data
public class ChannelMessageDto {
    private String message;

    private Date created;

    private String sender;
}
