package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.model.Quantity;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final IdService idService;
    private final CloudinaryService cloudinaryService;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(String id) throws InvalidIdException {
        Optional<Product> product = productRepo.findById(id);
        return product.orElseThrow(() -> new InvalidIdException("Product with " + id + " not found"));
    }

    public void deleteProduct(String id) throws InvalidIdException {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
        } else {
            throw new InvalidIdException("Product with " + id + " not found");
        }
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

    public Product updateProduct(MultipartFile multipartFile, String productId, ProductDTO productDTO) throws IOException {


        if (!productRepo.existsById(productId)) {
            throw new InvalidIdException("product not found");
        }
        Optional<Product> product = productRepo.findById(productId);
        if (product.isEmpty()) {
            throw new InvalidIdException("product with " + productId + " not found");
        }
        if (multipartFile == null || multipartFile.isEmpty()) {
            Product product1 = product.get()
                    .withId(productId)
                    .withName(productDTO.name())
                    .withPrice(productDTO.price())
                    .withQuantity(productDTO.quantity())
                    .withImageUrl(product.get().imageUrl());
            return productRepo.save(product1);
        }
        String imageUrl = cloudinaryService.uploadImage(multipartFile);
        Product product2 = product.get()
                .withId(productId)
                .withName(productDTO.name())
                .withPrice(productDTO.price())
                .withQuantity(productDTO.quantity())
                .withImageUrl(imageUrl);
        return productRepo.save(product2);

    }

    public Product addProduct(MultipartFile multipartFile, ProductDTO productDTO) throws IOException {

        String productId = idService.generateUUID();
        if (multipartFile == null || multipartFile.isEmpty()) {

            Product productWithoutPicture = new Product(productId, productDTO.name(), productDTO.price(), productDTO.quantity(), "http://res.cloudinary.com/dylxokrcs/image/upload/v1722871122/jtc7ycksrhoo5larwkyw.jpg", 0, 0);
            productRepo.save(productWithoutPicture);
            return productRepo.save(productWithoutPicture);
        }
        String imageUrl = cloudinaryService.uploadImage(multipartFile);
        Product productWithPicture = new Product(productId, productDTO.name(), productDTO.price(), productDTO.quantity(), imageUrl, 0, 0);
        productRepo.save(productWithPicture);
        return productRepo.save(productWithPicture);

    }

    public Product addRating(String productId, float rating) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new InvalidIdException("Product with " + productId + " not found"));
        Product updatedProduct = product.addRating(rating);
        return productRepo.save(updatedProduct);
    }

}
