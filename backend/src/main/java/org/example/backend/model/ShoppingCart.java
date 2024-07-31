package org.example.backend.model;


import java.util.Arrays;
import java.util.Objects;

public record ShoppingCart(
        OrderedProduct[] orderedProducts
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.deepEquals(orderedProducts, that.orderedProducts);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(orderedProducts);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "orderedProducts=" + Arrays.toString(orderedProducts) +
                '}';
    }
}
