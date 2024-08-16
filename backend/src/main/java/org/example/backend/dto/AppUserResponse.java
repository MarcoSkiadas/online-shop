package org.example.backend.dto;

import org.example.backend.model.AppUser;
import org.example.backend.model.ShoppingCart;

public record AppUserResponse(
        String id,
        String username,
        String role,
        ShoppingCart shoppingCart

) {
    public static AppUserResponse fromAppUser(AppUser appUser) {
        return new AppUserResponse(appUser.id(), appUser.username(), appUser.role(), appUser.shoppingCart());
    }
}
