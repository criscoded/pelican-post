package com.github.crisheight.gallery_api.controller;

import com.github.crisheight.gallery_api.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;
    private final String bucketName;

    // We inject the Service AND the bucket name from properties
    public ImageController(S3Service s3Service,
                           @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.s3Service = s3Service;
        this.bucketName = bucketName;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            // Call our service to do the heavy lifting
            String imageUrl = s3Service.uploadFile(bucketName, file);

            response.put("url", imageUrl);
            response.put("message", "Upload successful");
            return ResponseEntity.ok(response);
        }
        catch (IOException e) {
            response.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}