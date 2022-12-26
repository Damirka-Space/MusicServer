package com.dam1rka.musicserver.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDto {
    MultipartFile file;
}
