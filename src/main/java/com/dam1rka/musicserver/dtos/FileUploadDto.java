package com.dam1rka.musicserver.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class FileUploadDto {

    private Long id;

    private String name;
    private Date created = new Date();
    private String summary;

    private String contentId;
    private long contentLength;
    private String contentMimeType;
}
