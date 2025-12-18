package com.github.crisheight.gallery_api.service;

import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Template s3Template;

    public S3Service(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public String uploadFile(String bucketName, MultipartFile file) throws IOException {
        // Unique key to prevent overwriting
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // File stream uploads directly to S3
        try (InputStream inputStream = file.getInputStream()) {
            s3Template.upload(bucketName, key, inputStream);
        }

        // Return the URL to save into the database later
        return s3Template.download(bucketName, key).getURL().toString();
    }
}