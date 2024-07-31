package org.example.backend.dto;

import org.example.backend.model.OrderedProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public record ShoppingCartDTO(
        OrderedProduct[] orderedProducts
) {
    @Override
    public String toString() {
        return "ShoppingCartDTO{" +
                "orderedProducts=" + Arrays.toString(orderedProducts) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartDTO that = (ShoppingCartDTO) o;
        return Objects.deepEquals(orderedProducts, that.orderedProducts);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(orderedProducts);
    }
}
