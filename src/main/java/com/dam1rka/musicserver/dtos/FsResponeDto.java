package com.dam1rka.musicserver.dtos;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.Date;

@Data
public class FsResponeDto {
    private long id;
    private String contentId;
    private long contentLength;
    private String contentMimeType;
    private Date created;
    private String name;
    private String summary;
    private JsonObject _links;
}
