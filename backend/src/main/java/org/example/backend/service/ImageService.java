package org.example.backend.service;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ImageModel;
import org.example.backend.model.Image;
import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final CloudinaryService cloudinaryService;
    private final ProductRepo productRepo;

    public ResponseEntity<Map> uploadImage(ImageModel imageModel, String productId) {
        try {
            if (imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Image image = new Image();
            image.setName(imageModel.getName());
            image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
            if (image.getUrl() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (!productRepo.existsById(productId)) {
                return ResponseEntity.badRequest().build();
            }
            Optional<Product> product = productRepo.findById(productId);
            String imageUrl = image.getUrl();
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Product product1 = product.get()
                    .withId(productId)
                    .withName(product.get().name())
                    .withPrice(product.get().price())
                    .withQuantity(product.get().quantity())
                    .withImageUrl(imageUrl);
            productRepo.save(product1);
            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
