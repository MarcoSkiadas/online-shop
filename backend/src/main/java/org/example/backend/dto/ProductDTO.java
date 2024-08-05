package org.example.backend.dto;

import org.example.backend.model.Quantity;

public record ProductDTO(
        String name,
        float price,
        Quantity quantity,
        String imageUrl
) {
}
