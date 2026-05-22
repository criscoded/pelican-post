package com.github.crisheight.gallery_api.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    @Column(unique = true, nullable = false)
    private String s3Key;

    private String contentType;
    private String url;

    public Image() {}

    // Hibernate will map this to snake_case in Postgres
    public Image(String originalFileName, String s3Key, String contentType, String url) {
        this.originalFileName = originalFileName;
        this.s3Key = s3Key;
        this.contentType = contentType;
        this.url = url;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getS3Key() {
        return s3Key;
    }
    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image image)) return false;
        return Objects.equals(s3Key, image.s3Key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s3Key);
    }
} // End Image
