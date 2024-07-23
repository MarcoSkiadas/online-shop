package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.model.Product;
import org.example.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {return productService.updateProduct(id,productDTO);}
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {productService.deleteProduct(id);}
    @PostMapping()
    public Product addProduct(@RequestBody ProductDTO productDTO) { return productService.addProduct(productDTO);}
}
