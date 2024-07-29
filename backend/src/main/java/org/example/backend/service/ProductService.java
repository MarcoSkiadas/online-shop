package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

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

    public Product getProductById(String id) throws InvalidIdException {
        Optional<Product> product = productRepo.findById(id);
        return product.orElseThrow(() -> new InvalidIdException("Product with " + id + " not found"));
    }

    public Product updateProduct(String id, ProductDTO productDTO) throws InvalidIdException {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent()) {
            Product product1 = product.get()
                    .withId(id)
                    .withName(productDTO.name())
                    .withPrice(productDTO.price());
            productRepo.save(product1);
            return product1;
        } else {
            throw new InvalidIdException("Product with " + id + " not found");
        }
    }

    public void deleteProduct(String id) throws InvalidIdException {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
        } else {
            throw new InvalidIdException("Product with " + id + " not found");
        }
    }

    public Product addProduct(ProductDTO productDTO) {
        String productId = idService.generateUUID();
        Product product = new Product(productId, productDTO.name(), productDTO.price());
        productRepo.save(product);
        return product;
    }

    public List<Product> getAllProductsByIds(List<String> productIds) throws InvalidIdException {
        return productRepo.findAllById(productIds);
    }
}
