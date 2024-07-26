package org.example.backend.model;

public record AppUser(
        String id,
        String username,
        String role
) {
}
