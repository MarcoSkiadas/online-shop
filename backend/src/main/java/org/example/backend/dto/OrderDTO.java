package org.example.backend.dto;

import org.example.backend.model.OrderedProduct;


public record OrderDTO(
        OrderedProduct[] orderedProducts,
        float price,
        String userId
) {
}
