package com.github.crisheight.gallery_api.controller;

import com.github.crisheight.gallery_api.model.Image;
import com.github.crisheight.gallery_api.security.AppUserPrincipal;
import com.github.crisheight.gallery_api.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public List<Image> getMyImages(@AuthenticationPrincipal AppUserPrincipal principal) {
        return imageService.getAllImages(principal.getUserId());
    }

    @GetMapping("/{id}")
    public Image getImage(@PathVariable Long id, @AuthenticationPrincipal AppUserPrincipal principal) {
        return imageService.getImage(id, principal.getUserId());
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "note", required = false) String note,
                                             @RequestParam(value = "theme", required = false) String theme,
                                             @AuthenticationPrincipal AppUserPrincipal principal) throws IOException {
        Image savedImage = imageService.uploadImage(principal.getUserId(), file, note, theme);
        return ResponseEntity.ok(savedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id,
                                            @AuthenticationPrincipal AppUserPrincipal principal) throws IOException {
        imageService.deleteImage(id, principal.getUserId());
        return ResponseEntity.noContent().build(); // Returns HTTP 204
    }

} // End ImageController