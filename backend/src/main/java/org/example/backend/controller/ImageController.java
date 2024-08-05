package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ImageModel;
import org.example.backend.repository.ImageRepository;
import org.example.backend.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Map> upload(ImageModel imageModel) {
        try {
            return imageService.uploadImage(imageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}