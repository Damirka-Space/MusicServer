package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.FileDto;
import com.dam1rka.musicserver.dtos.FileUploadDto;
import com.dam1rka.musicserver.entities.ImageEnitiy;
import com.dam1rka.musicserver.entities.TrackEntity;
import com.dam1rka.musicserver.repositories.ImageRepository;
import com.dam1rka.musicserver.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class FileUploaderService {

    @Value("${file-server}")
    private String fileServer;

    @Value("${copy-boolean}")
    private boolean copyBoolean;

    private final WebClient webClient;

    private final TrackRepository trackRepository;

    private final ImageRepository imageRepository;

    private final ImageService imageService;

    private final FileService fileService;

    public FileUploaderService(WebClient webClient, TrackRepository trackRepository, ImageRepository imageRepository, ImageService imageService, FileService fileService) {
        this.webClient = webClient;
        this.trackRepository = trackRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
        this.fileService = fileService;
    }

    private enum FileType {
        Track,
        Image,
        MediumImage,
        SmallImage,
        File
    }

    private void send(String root, String path, FileUploadDto uploadDto, FileDto file) {

        try {
            // First send post request for entity creation
            Optional<String> res = webClient.post().uri(root)
                    .body(BodyInserters.fromValue(uploadDto))
                    .exchangeToMono(clientResponse -> Mono.just(clientResponse.headers().header("location").get(0))).blockOptional();

            // After entity creation sending content to them
            if(res.isPresent()) {
                String content = path + res.get().substring(res.get().lastIndexOf('/'));

                MultipartBodyBuilder b = new MultipartBodyBuilder();
                b.part("file", file.getFile().getResource());

                Optional<String> p = webClient.put()
                        .uri(content)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromValue(b.build()))
                        .retrieve()
                        .bodyToMono(String.class).blockOptional();

            }
            else {
                throw new RuntimeException("Can't send file: " + file.getFile().getOriginalFilename());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void upload(MultipartFile file, FileType type) {
        FileUploadDto uploadDto = new FileUploadDto();
        FileDto f = new FileDto();
        f.setFile(file);
        uploadDto.setName(file.getOriginalFilename());


        switch (type) {
            case Track -> {
                uploadDto.setContentMimeType("audio/mpeg");

                String path = fileServer + "tracks/";

                send(path, path , uploadDto, f);
            }
            case Image -> {
                uploadDto.setContentMimeType("image/jpeg");

                String path = fileServer + "images/";

                send(path, path, uploadDto, f);
            }
            case MediumImage -> {
                uploadDto.setContentMimeType("image/jpeg");

                String root = fileServer + "medium_images/";
                String path = fileServer + "mediumImages/";

                send(root, path, uploadDto, f);
            }
            case SmallImage -> {
                uploadDto.setContentMimeType("image/jpeg");

                String root = fileServer + "small_images/";
                String path = fileServer + "smallImages/";

                send(root, path, uploadDto, f);
            }
            case File -> {
                uploadDto.setContentMimeType("plain/text");

                String path = fileServer + "files/";

                send(path, path, uploadDto, f);
            }
            default -> {
                throw new RuntimeException("Invalid file type");
            }
        }
    }

    @PostConstruct
    public void startUp() {

        if(!copyBoolean)
            return;

        List<TrackEntity> trackList = trackRepository.findAll();

        for (TrackEntity t : trackList) {
            byte[] track = fileService.loadTrack(t.getId());

            MockMultipartFile trackFile = new MockMultipartFile(t.getTitle(),
                    t.getTitle() + ".mp3", "audio/mpeg", track);

            upload(trackFile, FileType.Track);
        }

        List<ImageEnitiy> imageList = imageRepository.findAll();

        for (ImageEnitiy e : imageList) {

            // Send big

            byte[] bigImg = imageService.getImage(e.getId());

            MockMultipartFile bigImageFile = new MockMultipartFile(e.getUrl(),
                    e.getUrl() + ".jpg", "image/jpeg", bigImg);

            upload(bigImageFile, FileType.Image);

            // Send medium

            byte[] mediumImg = imageService.getMediumImage(e.getId());

            MockMultipartFile mediumImageFile = new MockMultipartFile(e.getUrl(),
                    e.getUrl() + ".jpg", "image/jpeg", mediumImg);

            upload(mediumImageFile, FileType.MediumImage);


            // Send small

            byte[] smallImg = imageService.getSmailImage(e.getId());

            MockMultipartFile smallImageFile = new MockMultipartFile(e.getUrl(),
                    e.getUrl() + ".jpg", "image/jpeg", smallImg);

            upload(smallImageFile, FileType.SmallImage);

        }



    }

}
