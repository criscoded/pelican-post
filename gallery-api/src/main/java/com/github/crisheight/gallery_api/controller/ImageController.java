package com.github.crisheight.gallery_api.controller;

import com.github.crisheight.gallery_api.model.Image;
import com.github.crisheight.gallery_api.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Image savedImage = imageService.uploadImage(file);
        return ResponseEntity.ok(savedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) throws IOException {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build(); // Returns HTTP 204
    }
}