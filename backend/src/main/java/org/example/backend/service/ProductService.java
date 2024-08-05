package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.model.Quantity;
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
        Product product = new Product(productId, productDTO.name(), productDTO.price(), productDTO.quantity(), "https://res.cloudinary.com/dylxokrcs/image/upload/v1/folder_1/oatv35crxfavtf8gzrzz");
        productRepo.save(product);
        return product;
    }

    public List<Product> getAllProductsByIds(List<String> productIds) {
        return productRepo.findAllById(productIds);
    }

    public Product reduceProductOnStock(String productId, int productAmount) throws InvalidIdException {
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            Quantity newQuantity = new Quantity(product.get().quantity().amount() + (productAmount * -1), product.get().quantity().unit());
            Product product1 = product.get()
                    .withId(productId)
                    .withName(product.get().name())
                    .withPrice(product.get().price())
                    .withQuantity(newQuantity);
            return productRepo.save(product1);
        }
        throw new InvalidIdException("Product with " + productId + " not found");
    }

}
