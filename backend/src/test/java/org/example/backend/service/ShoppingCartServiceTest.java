package org.example.backend.service;

import org.example.backend.dto.OrderDTO;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.model.Order;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.OrderRepo;
import org.example.backend.repository.ShoppingCartRepo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingCartServiceTest {

    private final ShoppingCartRepo mockRepo = mock(ShoppingCartRepo.class);
    private final IdService mockUtils = mock(IdService.class);
    private final ShoppingCartService service = new ShoppingCartService(mockRepo,mockUtils);

    @Test
    void addShoppingCart_shouldShoppingCart_whenCalledWithShoppingCart() {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1",productIds);
        when(mockUtils.generateUUID()).thenReturn("1");
        when(mockRepo.save(expected)).thenReturn(expected);
        //WHEN
        ShoppingCart actual = service.addShoppingCart(new ShoppingCartDTO(productIds));
        //THEN
        assertEquals(expected, actual);
    }
}