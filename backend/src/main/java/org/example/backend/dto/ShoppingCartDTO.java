package org.example.backend.dto;

import org.example.backend.model.OrderedProduct;

import java.util.ArrayList;

public record ShoppingCartDTO(
        OrderedProduct[] orderedProducts
) {
}
