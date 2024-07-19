package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final IdService idService;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(String id) {
        Optional<Product> product = productRepo.findById(id);
        return product.orElseThrow();    }

    public Product updateProduct(String id, ProductDTO productDTO) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent()) {
            Product product1 = product.get()
                    .withId(id)
                    .withName(productDTO.name())
                    .withPrice(productDTO.price());
            productRepo.save(product1);
            return product1;
        }
        else {
            return null;
        }
    }
}
