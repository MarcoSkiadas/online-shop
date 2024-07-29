package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.AppUser;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appuser")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/shoppingCart/addProduct/{userId}/{productId}")
    public AppUser addProductToShoppingCart(@PathVariable String userId, @PathVariable String productId) throws InvalidIdException {
        return appUserService.addProductToShoppingCart(userId, productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/shoppingCart/removeProduct/{userId}/{productId}")
    public AppUser removeProductToShoppingCart(@PathVariable String userId, @PathVariable String productId) throws InvalidIdException {
        return appUserService.removeProductFromShoppingCart(userId, productId);
    }

}

