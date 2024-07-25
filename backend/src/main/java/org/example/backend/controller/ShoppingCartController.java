package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shoppingCart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ShoppingCart addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        return shoppingCartService.addShoppingCart(shoppingCartDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartService.getAllShoppingCarts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ShoppingCart getShoppingCartById(@PathVariable String id) throws InvalidIdException {
        return shoppingCartService.getShoppingCartById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/addProduct/{id}")
    public ShoppingCart addProductToShoppingCart(@PathVariable String id, @RequestBody ShoppingCartDTO shoppingCartDTO) throws InvalidIdException {
        return shoppingCartService.addProductToShoppingCart(id, shoppingCartDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/removeProduct/{id}")
    public ShoppingCart removeProductToShoppingCart(@PathVariable String id, @RequestBody ShoppingCartDTO shoppingCartDTO) throws InvalidIdException {
        return shoppingCartService.removeProductToShoppingCart(id, shoppingCartDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/products")
    public List<Product> getProductsFromShoppingCart(@PathVariable String id) throws InvalidIdException {
        return shoppingCartService.getProductsFromShoppingCart(id);
    }
}

