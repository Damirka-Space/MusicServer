package com.dam1rka.musicserver.dtos.channel;

import lombok.Data;

import java.util.Date;

@Data
public class ClientChannelPayload {

    private Long channelId;

    private Date created;

    private String content;

    private ChannelPayloadEnum action;

}
