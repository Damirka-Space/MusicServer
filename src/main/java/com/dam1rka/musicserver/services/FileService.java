package com.dam1rka.musicserver.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${music.format}")
    private String audioFormat;

    @Value("${image.format}")
    private String imageFormat;

    @Value("${file-dir}")
    private String fileDir;

    @Value("${track-dir}")
    private String tracksDir;

    @Value("${image-dir}")
    private String imageDir;

    private void write(byte[] file, Path filepath) throws RuntimeException {
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(MultipartFile file, Path filepath) throws RuntimeException {
        try {
            write(file.getBytes(), filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private byte[] read(Path filepath) throws RuntimeException {
        try (InputStream os = Files.newInputStream(filepath)) {
            return os.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeTrack(Long id, MultipartFile file) throws RuntimeException {
        write(file, Paths.get(tracksDir, "Track-" + id + "." + audioFormat));
    }

    private byte[] readTrack(Long id) throws RuntimeException {
        return read(Paths.get(tracksDir, "Track-" + id + "." + audioFormat));
    }

    public void saveFile(MultipartFile file) {
        write(file, Path.of(fileDir + file.getOriginalFilename()));
    }

    public void saveImage(MultipartFile file, String album) {
        write(file, Path.of(imageDir + album + "." + imageFormat));
    }
    public void saveImage(byte[] file, String album) {
        write(file, Path.of(imageDir + album + "." + imageFormat));
    }

    public byte[] loadImage(String album) {
        return read(Path.of(imageDir + album + "." + imageFormat));
    }

    public void saveTrack(Long id, MultipartFile file) throws RuntimeException {
        writeTrack(id, file);
    }

    public byte[] loadTrack(Long id) {
        return readTrack(id);
    }

}
