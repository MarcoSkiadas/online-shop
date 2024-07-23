package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.model.ShoppingCart;
import org.example.backend.service.ShoppingCartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shoppingCart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping()
    public ShoppingCart addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) { return shoppingCartService.addShoppingCart(shoppingCartDTO);}
}
