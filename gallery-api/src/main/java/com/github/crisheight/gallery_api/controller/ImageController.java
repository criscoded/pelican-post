package com.github.crisheight.gallery_api.controller;

import com.github.crisheight.gallery_api.model.Image;
import com.github.crisheight.gallery_api.repository.ImageRepository;
import com.github.crisheight.gallery_api.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final String bucketName;

    // Injecting the Service, ImageRepository, and the bucket name from properties
    public ImageController(S3Service s3Service,
                           ImageRepository imageRepository,
                           @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.s3Service = s3Service;
        this.imageRepository = imageRepository;
        this.bucketName = bucketName;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            // Upload file; get key
            String key = s3Service.uploadFile(bucketName, file);

            // Generate temp url
            String signedUrl = s3Service.createPresignedGetUrl(bucketName, key);

            // Save the metadata
            Image image = new Image(file.getOriginalFilename(), signedUrl, key);
            imageRepository.save(image); // SQL insert

            response.put("url", signedUrl);
            response.put("message", "Upload successful");
            return ResponseEntity.ok(response);
        }
        catch (IOException e) {
            response.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping
    public List<Image> getAllImages() {
        List<Image> images = imageRepository.findAll();

        // Loop and refresh the signed URLs
        for (Image image : images) {
            String signedUrl = s3Service.createPresignedGetUrl(bucketName, image.getS3Key());
            image.setUrl(signedUrl); // Update the object in memory before returning (don't save to DB)
        }

        return images;
    }
} // End ImageController