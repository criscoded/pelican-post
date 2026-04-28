package com.github.crisheight.gallery_api.service;

import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Template s3Template) {
        this.s3Template = s3Template;
    }


    public void uploadFile(String key, MultipartFile file) throws IOException {
        s3Template.upload(bucketName, key, file.getInputStream());
    }

    public void deleteFile(String key) {
        s3Template.deleteObject(bucketName, key);
    }

} // End S3Service
