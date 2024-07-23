package org.example.backend.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public record ShoppingCart(
        @Id
        String id,
        ArrayList<String> productIds
) {
}
