package com.github.crisheight.gallery_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,
            unique = true,
            columnDefinition = "TEXT")
    private String fileName;

    @Column(nullable = false,
            columnDefinition = "TEXT")
    private String s3Key;

    @Column(nullable = false,
            columnDefinition = "TEXT")
    private String url;

    private LocalDateTime createdAt;

    public Image() {}

    // Hibernate will map this to snake_case in Postgres
    public Image(String fileName, String url, String s3Key) {
        this.fileName = fileName;
        this.url = url;
        this.s3Key = s3Key;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} // End Image
