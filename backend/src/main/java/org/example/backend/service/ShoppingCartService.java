package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.ProductRepo;
import org.example.backend.repository.ShoppingCartRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepo shoppingCartRepo;
    private final IdService idService;
    private final ProductRepo productRepo;

    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartRepo.findAll();
    }

    public ShoppingCart getShoppingCartById(String id) throws InvalidIdException {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepo.findById(id);
        return shoppingCart.orElseThrow(() -> new InvalidIdException("Shopping Cart with " + id + " not found"));
    }

    public ShoppingCart addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        String id = idService.generateUUID();
        ShoppingCart s = new ShoppingCart(id, shoppingCartDTO.productIds());
        return shoppingCartRepo.save(s);
    }

    public ShoppingCart addProductToShoppingCart(String id, ShoppingCartDTO shoppingCartDTO) throws InvalidIdException {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepo.findById(id);
        if (shoppingCart.isPresent()) {
            ArrayList<String> productIds = shoppingCart.get().productIds();
            boolean exists = false;
            for (String productId : productIds) {
                if (shoppingCartDTO.productIds().contains(productId)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                productIds.add(shoppingCartDTO.productIds().getFirst());
            }
            ShoppingCart shoppingCart1 = shoppingCart.get()
                    .withId(id)
                    .withProductIds(productIds);
            shoppingCartRepo.save(shoppingCart1);
            return shoppingCart1;
        } else {
            throw new InvalidIdException("Shopping Cart with " + id + " not found");
        }
    }

    public ShoppingCart removeProductToShoppingCart(String id, ShoppingCartDTO shoppingCartDTO) throws InvalidIdException {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepo.findById(id);
        if (shoppingCart.isPresent()) {
            ArrayList<String> productIds = shoppingCart.get().productIds();
            boolean exists = false;
            for (String productId : productIds) {
                if (shoppingCartDTO.productIds().contains(productId)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                productIds.remove(shoppingCartDTO.productIds().getFirst());
            }
            ShoppingCart shoppingCart1 = shoppingCart.get()
                    .withId(id)
                    .withProductIds(productIds);
            shoppingCartRepo.save(shoppingCart1);
            return shoppingCart1;
        } else {
            throw new InvalidIdException("Shopping Cart with " + id + " not found");
        }
    }

    public List<Product> getProductsFromShoppingCart(String id) throws InvalidIdException {
        ShoppingCart cart = shoppingCartRepo.findById(id).orElseThrow(() -> new InvalidIdException("Shopping Cart with " + id + " not found"));
        return productRepo.findAllById(cart.productIds());
    }
}
