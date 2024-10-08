package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AppUserResponse;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appuser")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/shoppingCart/addProduct/{userId}/{productId}/{amount}")
    public AppUserResponse addProductToShoppingCart(@PathVariable String userId, @PathVariable String productId, @PathVariable int amount) throws InvalidIdException {
        return appUserService.addProductToShoppingCart(userId, productId, amount);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/shoppingCart/removeProduct/{userId}/{productId}")
    public AppUserResponse removeProductToShoppingCart(@PathVariable String userId, @PathVariable String productId) throws InvalidIdException {
        return appUserService.removeProductFromShoppingCart(userId, productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/shoppingCart/removeProduct/{userId}")
    public AppUserResponse removeAllProductsFromShoppingCart(@PathVariable String userId) throws InvalidIdException {
        return appUserService.removeAllProductsFromShoppingCart(userId);
    }


}

