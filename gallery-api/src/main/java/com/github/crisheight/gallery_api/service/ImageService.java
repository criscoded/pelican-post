package com.github.crisheight.gallery_api.service;

import com.github.crisheight.gallery_api.model.Image;
import com.github.crisheight.gallery_api.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
        s3Service.uploadFile(s3Key, file);

        var image = new Image(originalFileName, s3Key, file.getContentType(), null);
        var savedImage = imageRepository.save(image);

        String presignedUrl = s3Service.createPresignedUrl(savedImage.getS3Key());
        savedImage.setUrl(presignedUrl);

        return savedImage;
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));

        s3Service.deleteFile(image.getS3Key());
        imageRepository.delete(image);
    }

    public List<Image> getAllImages() {
        List<Image> imageList = imageRepository.findAll();

        for(Image image : imageList) {
            String presignedUrl = s3Service.createPresignedUrl(image.getS3Key());
            image.setUrl(presignedUrl);
        }

        return imageList;
    }
} // End ImageService