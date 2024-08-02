package org.example.backend.model;

import java.util.Objects;

public record OrderedProduct(
        String productId,
        int amount
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedProduct that = (OrderedProduct) o;
        return amount == that.amount && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, amount);
    }
}
