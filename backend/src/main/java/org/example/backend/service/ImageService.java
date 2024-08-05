package org.example.backend.service;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final CloudinaryService cloudinaryService;
    private final ProductRepo productRepo;

    public Product uploadImage(MultipartFile multipartFile, String productId, ProductDTO productDTO) throws IOException {

        String imageUrl = cloudinaryService.uploadImage(multipartFile);
        if (!productRepo.existsById(productId)) {
            throw new InvalidIdException("product not found");
        }
        Optional<Product> product = productRepo.findById(productId);
        if (product.isEmpty()) {
            throw new InvalidIdException("product not found");
        }
        if (multipartFile.isEmpty()) {
            Product product1 = product.get()
                    .withId(productId)
                    .withName(productDTO.name())
                    .withPrice(productDTO.price())
                    .withQuantity(productDTO.quantity())
                    .withImageUrl(product.get().imageUrl());
            return productRepo.save(product1);
        }
        Product product2 = product.get()
                .withId(productId)
                .withName(productDTO.name())
                .withPrice(productDTO.price())
                .withQuantity(productDTO.quantity())
                .withImageUrl(imageUrl);
        return productRepo.save(product2);
    }
}
