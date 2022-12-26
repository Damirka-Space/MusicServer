package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.ImageEnitiy;
import com.dam1rka.musicserver.repositories.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Objects;

@Service
public class ImageService {

    @Value("${image.resolution.min}")
    private Integer min;

    @Value("${image.extension.min}")
    private String minExt;

    @Value("${image.resolution.medium}")
    private Integer medium;

    @Value("${image.extension.medium}")
    private String mediumExt;

    @Value("${image.format}")
    private String imageFormat;

    private final ImageRepository imageRepository;

    private final FileService fileService;

    @Autowired
    public ImageService(ImageRepository imageRepository, FileService fileService) {
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    private byte[] resizeAndSaveImage(String extension, String url, int targetWidth, int targetHeight) throws RuntimeException {
        try {
            byte[] bigImage = fileService.loadImage(url);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(ImageIO.read(new ByteArrayInputStream(bigImage)))
                    .size(targetWidth, targetHeight)
                    .outputFormat("jpg")
                    .outputQuality(1)
                    .toOutputStream(outputStream);

            byte[] smallImage = outputStream.toByteArray();

            fileService.saveImage(smallImage, extension + "-" + url);

            return smallImage;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ImageEnitiy image(Long id) {
        ImageEnitiy img = imageRepository.findById(id).orElse(null);

        if(Objects.isNull(img))
            throw new RuntimeException("Image not found");

        return img;
    }


    // Returns image of 1024x1024
    public byte[] getImage(Long id) {
        ImageEnitiy img = image(id);

        return fileService.loadImage(img.getUrl());
    }

    // Returns image of 128x128
    public byte[] getSmailImage(Long id) {
        ImageEnitiy img = image(id);

        byte[] smallImage;

        try {
            smallImage = fileService.loadImage(minExt + "-" + img.getUrl());
        } catch (Exception e) {
            smallImage = resizeAndSaveImage(minExt, img.getUrl(), min, min);
        }

        return smallImage;
    }

    // Returns image of 256x256
    public byte[] getMediumImage(Long id) {
        ImageEnitiy img = image(id);

        byte[] mediumImage;

        try {
            mediumImage = fileService.loadImage(mediumExt + "-" + img.getUrl());
        } catch (Exception e) {
            mediumImage = resizeAndSaveImage(mediumExt, img.getUrl(), medium, medium);
        }

        return mediumImage;
    }

    public ImageEnitiy saveImage(MultipartFile image, String album) {
        Date now = new Date();
        ImageEnitiy imageEnitiy = imageRepository.findByUrl(album);

        if(Objects.isNull(imageEnitiy)) {
            imageEnitiy = new ImageEnitiy();
            imageEnitiy.setUrl(album);
            imageEnitiy.setCreated(now);
            imageEnitiy.setUpdated(now);
            imageEnitiy = imageRepository.save((imageEnitiy));
            fileService.saveImage(image, album);

            // create small and medium images
            resizeAndSaveImage(minExt, album, min, min);
            resizeAndSaveImage(mediumExt, album, medium, medium);
        }
        return imageEnitiy;
    }

}
