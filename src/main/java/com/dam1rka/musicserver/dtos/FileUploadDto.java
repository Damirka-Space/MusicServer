package com.dam1rka.musicserver.dtos;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
