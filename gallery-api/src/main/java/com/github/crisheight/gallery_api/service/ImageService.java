package com.github.crisheight.gallery_api.service;

import com.github.crisheight.gallery_api.model.Image;
import com.github.crisheight.gallery_api.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public ImageService(ImageRepository imageRepository, S3Service s3Service) {
        this.imageRepository = imageRepository;
        this.s3Service = s3Service;
    }

    public Image uploadImage(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String s3Key = uuid + extension;
        String imageUrl = s3Service.uploadFile(s3Key, file);

        Image image = new Image(originalFileName, s3Key, file.getContentType(), imageUrl);
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));

        s3Service.deleteFile(image.getS3Key());

        imageRepository.delete(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
}