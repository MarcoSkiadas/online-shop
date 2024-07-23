package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ProductDTO;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.model.Order;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shoppingCart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping()
    public ShoppingCart addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) { return shoppingCartService.addShoppingCart(shoppingCartDTO);}

    @GetMapping()
    public List<ShoppingCart> getAllShoppingCarts() { return shoppingCartService.getAllShoppingCarts(); }

    @GetMapping("/{id}")
    public ShoppingCart getShoppingCartById(@PathVariable String id){ return shoppingCartService.getShoppingCartById(id);}

    @PutMapping("/{id}")
    public ShoppingCart updateShoppingCart(@PathVariable String id, @RequestBody ShoppingCartDTO shoppingCartDTO) {return shoppingCartService.updateShoppingCart(id,shoppingCartDTO);}
}
