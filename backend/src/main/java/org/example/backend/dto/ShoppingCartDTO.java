package org.example.backend.dto;

import java.util.ArrayList;

public record ShoppingCartDTO(
        ArrayList<String> productIds
) {
}
