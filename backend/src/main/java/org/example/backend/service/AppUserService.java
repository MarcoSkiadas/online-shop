package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.AppUser;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final ProductRepo productRepo;

    public AppUser addProductToShoppingCart(String userId, String productId) throws InvalidIdException {
        AppUser appuser = appUserRepository.findById(userId).orElseThrow(() -> new InvalidIdException("User with " + userId + " not found"));
        if (productRepo.existsById(productId)) {
            appuser.shoppingCart().productIds().add(productId);
            return appUserRepository.save(appuser);
        }
        throw new InvalidIdException("Product with " + productId + " not found");
    }

    public AppUser removeProductFromShoppingCart(String userId, String productId) throws InvalidIdException {
        AppUser appuser = appUserRepository.findById(userId).orElseThrow(() -> new InvalidIdException("User with " + userId + " not found"));
        for (String products : appuser.shoppingCart().productIds()) {
            if (productId.equals(products)) {
                appuser.shoppingCart().productIds().remove(productId);
                return appUserRepository.save(appuser);
            }

        }
        throw new InvalidIdException("Product with " + productId + " not found in Shopping Cart");
    }

    public AppUser removeAllProductsFromShoppingCart(String userId) throws InvalidIdException {
        AppUser appuser = appUserRepository.findById(userId).orElseThrow(() -> new InvalidIdException("User with " + userId + " not found"));
        if (!appuser.shoppingCart().productIds().isEmpty()) {
            appuser.shoppingCart().productIds().clear();
            return appUserRepository.save(appuser);
        }
        throw new InvalidIdException("User with " + userId + " not found");
    }

}
