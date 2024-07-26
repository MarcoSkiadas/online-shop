package org.example.backend.model;

import java.util.ArrayList;


public record ShoppingCart(
        ArrayList<String> productIds
) {
}
