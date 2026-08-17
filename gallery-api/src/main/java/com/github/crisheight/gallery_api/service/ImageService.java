package com.github.crisheight.gallery_api.service;

import com.github.crisheight.gallery_api.exception.ImageNotFoundException;
import com.github.crisheight.gallery_api.exception.UnsupportedContentTypeException;
import com.github.crisheight.gallery_api.model.Image;
import com.github.crisheight.gallery_api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageService {

    private static final Map<String, String> ALLOWED_CONTENT_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif",
            "image/avif", ".avif"
    );

    private static final Map<String, String> ALLOWED_THEMES = Map.of(
            "airmail", "airmail",
            "mushroom", "mushroom",
            "star", "star",
            "melody", "melody",
            "nook", "nook",
            "town-hall", "town-hall"
    );

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Value("${app.cdn.base-url}")
    private String cdnBaseUrl;

    public ImageService(ImageRepository imageRepository, S3Service s3Service) {
        this.imageRepository = imageRepository;
        this.s3Service = s3Service;
    }

    public Image uploadImage(Long ownerId, MultipartFile file, String note, String theme) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.containsKey(contentType.toLowerCase())) {
            throw new UnsupportedContentTypeException(contentType);
        }

        if (note != null && note.length() > 200) {
            throw new IllegalArgumentException("Note must be at most 200 characters");
        }
        if (theme != null && !theme.isBlank() && !ALLOWED_THEMES.containsKey(theme)) {
            throw new IllegalArgumentException("Invalid theme");
        }

        String extension = ALLOWED_CONTENT_TYPES.get(contentType.toLowerCase());
        String s3Key = UUID.randomUUID() + extension;
        s3Service.uploadFile(s3Key, file);

        var image = new Image(ownerId, file.getOriginalFilename(), s3Key, contentType, null, note, theme);
        var savedImage = imageRepository.save(image);

        savedImage.setUrl(cdnBaseUrl + savedImage.getS3Key());

        return savedImage;
    }

    public List<Image> getAllImages(Long ownerId) {
        List<Image> imageList = imageRepository.findByOwnerIdOrderByIdDesc(ownerId);

        for (Image image : imageList) {
            image.setUrl(cdnBaseUrl + image.getS3Key());
        }

        return imageList;
    }

    public Image getImage(Long id, Long ownerId) {
        return imageRepository.findByIdAndOwnerId(id, ownerId)
                .map(image -> {
                    image.setUrl(cdnBaseUrl + image.getS3Key());
                    return image;
                })
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    public void deleteImage(Long id, Long ownerId) throws IOException {
        Image image = imageRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ImageNotFoundException(id));

        s3Service.deleteFile(image.getS3Key());
        imageRepository.delete(image);
    }
} // End ImageService