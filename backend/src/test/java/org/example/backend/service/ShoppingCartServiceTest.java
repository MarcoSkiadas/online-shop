package org.example.backend.service;

import org.example.backend.dto.ProductDTO;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Order;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.ProductRepo;
import org.example.backend.repository.ShoppingCartRepo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ShoppingCartServiceTest {

    private final ShoppingCartRepo mockRepo = mock(ShoppingCartRepo.class);
    private final IdService mockUtils = mock(IdService.class);
    private final ProductRepo mockProductRepo = mock(ProductRepo.class);
    private final ShoppingCartService service = new ShoppingCartService(mockRepo, mockUtils, mockProductRepo);

    @Test
    void addShoppingCart_shouldShoppingCart_whenCalledWithShoppingCart() {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1", productIds);
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
    void getShoppingCartById_shouldReturnShoppingCart_whenCalledById() throws InvalidIdException {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1", productIds);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        ShoppingCart actual = service.getShoppingCartById("1");
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void getShoppingCartById_shouldReturnException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.getShoppingCartById("1"));
        verify(mockRepo).findById("1");
    }

    @Test
    void addProductToShoppingCart_shouldAddProductToShoppingCart_whenCalledById() throws InvalidIdException {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1", productIds);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        when(mockRepo.save(expected)).thenReturn(expected);
        ArrayList<String> productIdsA = new ArrayList<>();
        ShoppingCart actual = new ShoppingCart("1", productIdsA);
        productIdsA.add("1");
        productIdsA.add("2");
        ArrayList<String> productIdsB = new ArrayList<>();
        productIdsB.add("3");
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(productIdsB);
        //WHEN
        actual = service.addProductToShoppingCart("1", shoppingCartDTO);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void addProductToShoppingCart_shouldThrowException_whenCalledByWrongId() throws InvalidIdException {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.addProductToShoppingCart("1", new ShoppingCartDTO(productIds)));
        verify(mockRepo).findById("1");
    }

    @Test
    void removeProductToShoppingCart_shouldRemoveProductToShoppingCart_whenCalledById() throws InvalidIdException {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        ShoppingCart expected = new ShoppingCart("1", productIds);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        when(mockRepo.save(expected)).thenReturn(expected);
        ArrayList<String> productIdsA = new ArrayList<>();
        ShoppingCart actual = new ShoppingCart("1", productIdsA);
        productIdsA.add("1");
        productIdsA.add("2");
        ArrayList<String> productIdsB = new ArrayList<>();
        productIdsB.add("2");
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(productIdsB);
        //WHEN
        actual = service.removeProductToShoppingCart("1", shoppingCartDTO);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void removeProductToShoppingCart_shouldThrowException_whenCalledByWrongId() throws InvalidIdException {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.removeProductToShoppingCart("1", new ShoppingCartDTO(productIds)));
        verify(mockRepo).findById("1");
    }

    @Test
    void getProductsFromShoppingCart_shouldReturnProducts_whenCalledByOrdersId() throws InvalidIdException {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1", productIds);
        Product expectedProduct1 = new Product("1", "Rasenmäher", 22);
        Product expectedProduct2 = new Product("2", "Tee", 22);
        Product expectedProduct3 = new Product("3", "Tasse", 22);
        ArrayList<Product> products = new ArrayList<>();
        products.add(expectedProduct1);
        products.add(expectedProduct2);
        products.add(expectedProduct3);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        when(mockProductRepo.findAllById(expected.productIds())).thenReturn(products);
        service.getProductsFromShoppingCart(expected.id());
        verify(mockRepo).findById(expected.id());
        verify(mockProductRepo).findAllById(expected.productIds());
    }

    @Test
    void getProductsFromShoppingCart_shouldReturnExceptions_whenCalledByWrongOrdersId() throws InvalidIdException {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        ShoppingCart expected = new ShoppingCart("1", productIds);
        Product expectedProduct1 = new Product("1", "Rasenmäher", 22);
        Product expectedProduct2 = new Product("2", "Tee", 22);
        Product expectedProduct3 = new Product("3", "Tasse", 22);
        ArrayList<Product> products = new ArrayList<>();
        products.add(expectedProduct1);
        products.add(expectedProduct2);
        products.add(expectedProduct3);
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        when(mockProductRepo.findAllById(expected.productIds())).thenReturn(products);
        assertThrows(InvalidIdException.class, () -> service.getProductsFromShoppingCart("1"));
        verify(mockRepo).findById("1");
    }

}