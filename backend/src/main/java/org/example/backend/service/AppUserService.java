package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.AppUser;
import org.example.backend.model.OrderedProduct;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.repository.ProductRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final ProductRepo productRepo;

    public AppUser addProductToShoppingCart(String userId, String productId, int amount) throws InvalidIdException {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new InvalidIdException("User with " + userId + " not found"));

        if (productRepo.existsById(productId)) {
            ShoppingCart shoppingCart = appUser.shoppingCart();
            List<OrderedProduct> orderedProducts = new ArrayList<>(List.of(shoppingCart.orderedProducts()));

            for (OrderedProduct orderedProduct : orderedProducts) {
                if (orderedProduct.productId().equals(productId)) {
                    throw new InvalidIdException("Product with " + productId + " already exists");
                }
            }

            orderedProducts.add(new OrderedProduct(productId, amount));
            ShoppingCart updatedShoppingCart = new ShoppingCart(orderedProducts.toArray(new OrderedProduct[0]));
            AppUser updatedAppUser = new AppUser(appUser.id(), appUser.username(), appUser.role(), updatedShoppingCart);

            return appUserRepository.save(updatedAppUser);
        }
        throw new InvalidIdException("Product with " + productId + " not found");
    }


    public AppUser removeProductFromShoppingCart(String userId, String productId) throws InvalidIdException {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new InvalidIdException("User with " + userId + " not found"));

        ShoppingCart shoppingCart = appUser.shoppingCart();

        List<OrderedProduct> orderedProducts = new ArrayList<>(List.of(shoppingCart.orderedProducts()));

        boolean productRemoved = orderedProducts.removeIf(orderedProduct -> orderedProduct.productId().equals(productId));

        if (productRemoved) {
            ShoppingCart updatedShoppingCart = new ShoppingCart(orderedProducts.toArray(new OrderedProduct[0]));
            AppUser updatedAppUser = new AppUser(appUser.id(), appUser.username(), appUser.role(), updatedShoppingCart);
            return appUserRepository.save(updatedAppUser);
        }

        throw new InvalidIdException("Product with " + productId + " not found in Shopping Cart");
    }

    public AppUser removeAllProductsFromShoppingCart(String userId) throws InvalidIdException {
        AppUser appuser = appUserRepository.findById(userId)
                .orElseThrow(() -> new InvalidIdException("User with " + userId + " not found"));
        AppUser NewAppuser = new AppUser(appuser.id(), appuser.username(), appuser.role(), new ShoppingCart(new OrderedProduct[0]));
        return appUserRepository.save(NewAppuser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not Found!"));
        return new AppUser(user.username(), user.password(), Collections.emptyList());
    }
}
