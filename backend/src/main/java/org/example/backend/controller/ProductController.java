package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) throws InvalidIdException {
        return productService.getProductById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) throws InvalidIdException {
        productService.deleteProduct(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/shoppingCart")
    public List<Product> getAllProductsByIds(@RequestParam List<String> productIds) {
        return productService.getAllProductsByIds(productIds);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/shoppingCart/{productId}/{productAmount}")
    public Product reduceProductOnStock(@PathVariable String productId, @PathVariable int productAmount) throws InvalidIdException {
        return productService.reduceProductOnStock(productId, productAmount);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/upload/{productId}")
    public Product updateProduct(@RequestPart(name = "file", required = false) MultipartFile multipartFile, @PathVariable String productId, @RequestPart("product") ProductDTO productDTO) throws IOException {
        return productService.updateProduct(multipartFile, productId, productDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload")
    public Product addProduct(@RequestPart(name = "file", required = false) MultipartFile multipartFile, @RequestPart("product") ProductDTO productDTO) throws IOException {
        return productService.addProduct(multipartFile, productDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/rate")
    public Product rateProduct(@PathVariable String id, @RequestParam float newRating, @RequestParam(required = false) String commentary) throws IOException {
        return productService.addRating(id, newRating, commentary);
    }
}
