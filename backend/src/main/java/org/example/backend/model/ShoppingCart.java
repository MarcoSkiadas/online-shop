package org.example.backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@With
public record ShoppingCart(
        @Id
        String id,
        ArrayList<String> productIds
) {
}
