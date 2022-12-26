package com.dam1rka.musicserver.dtos;


import lombok.Data;

import java.util.List;

public class FileResponseDto extends FileUploadDto {

    public List<List<String>> _links;

}
