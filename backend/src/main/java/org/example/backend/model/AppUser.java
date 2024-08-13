package org.example.backend.model;

import org.springframework.data.annotation.Id;

public record AppUser(
        @Id
        String id,
        String username,
        String password,
        String role,
        ShoppingCart shoppingCart

) {
}
