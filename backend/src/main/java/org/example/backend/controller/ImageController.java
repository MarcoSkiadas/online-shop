package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ImageModel;
import org.example.backend.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload/{productId}")
    public ResponseEntity<Map> upload(ImageModel imageModel, @PathVariable String productId) {
        try {
            return imageService.uploadImage(imageModel, productId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}