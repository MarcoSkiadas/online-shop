package org.example.backend.dto;

import org.example.backend.model.OrderedProduct;

import java.util.Arrays;
import java.util.Objects;


public record OrderDTO(
        OrderedProduct[] orderedProducts,
        float price,
        String userId
) {

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderedProducts=" + Arrays.toString(orderedProducts) +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Float.compare(price, orderDTO.price) == 0 && Objects.equals(userId, orderDTO.userId) && Objects.deepEquals(orderedProducts, orderDTO.orderedProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(orderedProducts), price, userId);
    }
}
