package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final IdService idService;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
}
