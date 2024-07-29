package org.example.backend.model;

import org.springframework.data.annotation.Id;

public record AppUser(
        @Id
        String id,
        String username,
        String role,
        ShoppingCart shoppingCart

) {
}
