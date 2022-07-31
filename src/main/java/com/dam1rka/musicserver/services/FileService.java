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

    @Value("${file-dir}")
    private String fileDir;

    @Value("${track-dir}")
    private String tracksDir;

    @Value("${image-dir}")
    private String imageDir;

    private void write(MultipartFile file, Path dir) throws RuntimeException {
        Path filepath = Paths.get(dir.toString(), file.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private byte[] read(String file, Path dir) throws RuntimeException {
        Path filepath = Paths.get(dir.toString(), file);

        try (InputStream os = Files.newInputStream(filepath)) {
            return os.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveFile(MultipartFile file) {
        write(file, Path.of(fileDir));
    }

    public void saveTrack(Long id, MultipartFile file) throws RuntimeException {
        writeTrack(id, file, Path.of(tracksDir));
    }

    public void saveImage(MultipartFile file) {
        write(file, Path.of(imageDir));
    }

    public byte[] loadImage(String url) {
        return read(url, Path.of(imageDir));
    }

    private void writeTrack(Long id, MultipartFile file, Path dir) throws RuntimeException {
        Path filepath = Paths.get(dir.toString(), "Track-" + id + ".mp3");

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readTrack(Long id, Path dir) throws RuntimeException {
        Path filepath = Paths.get(dir.toString(), "Track-" + id + ".mp3");

        try (InputStream os = Files.newInputStream(filepath)) {
            return os.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadTrack(Long id) {
        return readTrack(id, Path.of(tracksDir));
    }

}
