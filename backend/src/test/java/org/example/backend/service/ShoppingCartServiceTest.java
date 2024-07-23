package org.example.backend.service;

import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.ShoppingCartRepo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @Test
    void getAllShoppingCarts_shouldReturnEmptyList_whenCalledInitially() {
        //GIVEN
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());
        //WHEN
        List<ShoppingCart> actual = service.getAllShoppingCarts();
        //THEN
        assertEquals(Collections.EMPTY_LIST, actual);
    }
    @Test
    void getShoppingCartById_shouldReturnShoppingCart_whenCalledById() {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1",productIds);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        ShoppingCart actual = service.getShoppingCartById("1");
        //THEN
        assertEquals(expected, actual);
    }
    @Test
    void updateShoppingCart_shouldAddProductToShoppingCart_whenCalledById() {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1",productIds);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        when(mockRepo.save(expected)).thenReturn(expected);
        ArrayList<String> productIdsA = new ArrayList<>();
        ShoppingCart actual = new ShoppingCart("1",productIdsA);
        productIdsA.add("1");
        productIdsA.add("2");
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(productIds);
        //WHEN
        actual = service.addProductToShoppingCart("1",shoppingCartDTO);
        //THEN
        assertEquals(expected, actual);
    }
}