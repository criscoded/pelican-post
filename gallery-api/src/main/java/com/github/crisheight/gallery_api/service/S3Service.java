package com.github.crisheight.gallery_api.service;

import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Service
public class S3Service {

    private final S3Template s3Template;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Template s3Template, S3Presigner s3Presigner) {
        this.s3Template = s3Template;
        this.s3Presigner = s3Presigner;
    }


    public String uploadFile(String key, MultipartFile file) throws IOException {
        s3Template.upload(bucketName, key, file.getInputStream());
        return key;
    }

    public void deleteFile(String key) {
        s3Template.deleteObject(bucketName, key);
    }

    // Generates a temporary link valid for 60 minutes
    public String createPresignedUrl(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(objectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
} // End S3Service