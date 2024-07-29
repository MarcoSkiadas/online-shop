package org.example.backend.dto;

import java.util.ArrayList;

public record OrderDTO(
        ArrayList<String> productIds,
        float price,
        String userId
) {
}
