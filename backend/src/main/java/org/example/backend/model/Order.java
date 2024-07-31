package org.example.backend.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public record Order(
        @Id
        String id,
        OrderedProduct[] orderedProducts,
        float price,
        String userId
) {
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderedProducts=" + Arrays.toString(orderedProducts) +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Float.compare(price, order.price) == 0 && Objects.equals(id, order.id) && Objects.equals(userId, order.userId) && Objects.deepEquals(orderedProducts, order.orderedProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(orderedProducts), price, userId);
    }
}
