package com.dam1rka.musicserver.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ListenHistoryDto {
    private Date listened;
    private String trackName;
}
