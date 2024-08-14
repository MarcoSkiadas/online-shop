package org.example.backend.dto;

import org.example.backend.model.OrderedProduct;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

    @Test
    void testToString() {

        OrderedProduct[] orderedProducts = new OrderedProduct[]{new OrderedProduct("1", 2), new OrderedProduct("2", 3)};
        float price = 99.99f;
        String userId = "user123";

        String expected = "OrderDTO{" +
                "orderedProducts=" + Arrays.toString(orderedProducts) +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                '}';

        OrderDTO order = new OrderDTO(orderedProducts, price, userId);

        assertEquals(expected, order.toString());
    }

    @Test
    void testEquals() {
        OrderedProduct[] orderedProducts1 = new OrderedProduct[]{new OrderedProduct("1", 2), new OrderedProduct("2", 3)};
        float price1 = 99.99f;
        String userId1 = "user123";

        OrderDTO order1 = new OrderDTO(orderedProducts1, price1, userId1);
        OrderDTO order2 = new OrderDTO(orderedProducts1, price1, userId1);

        assertEquals(order1, order2);

        OrderDTO order3 = new OrderDTO(orderedProducts1, 89.99f, userId1);

        assertNotEquals(order1, order3);
    }

    @Test
    void testHashCode() {
        OrderedProduct[] orderedProducts1 = new OrderedProduct[]{new OrderedProduct("1", 2), new OrderedProduct("2", 3)};
        float price1 = 99.99f;
        String userId1 = "user123";

        OrderDTO order1 = new OrderDTO(orderedProducts1, price1, userId1);
        OrderDTO order2 = new OrderDTO(orderedProducts1, price1, userId1);

        assertEquals(order1.hashCode(), order2.hashCode());

        OrderDTO order3 = new OrderDTO(orderedProducts1, 89.99f, userId1);

        assertNotEquals(order1.hashCode(), order3.hashCode());
    }
}