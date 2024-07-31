package org.example.backend.model;


public record ShoppingCart(
        OrderedProduct[] orderedProducts
) {
}
