package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.model.Product;
import org.example.backend.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload/{productId}")
    public Product upload(@RequestPart(name = "file", required = false) MultipartFile multipartFile, @PathVariable String productId, @RequestPart("product") ProductDTO productDTO) throws IOException {
        return imageService.uploadImage(multipartFile, productId, productDTO);
    }
}