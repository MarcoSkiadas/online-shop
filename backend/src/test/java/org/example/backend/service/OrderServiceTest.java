package org.example.backend.service;

import org.example.backend.dto.OrderDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.*;
import org.example.backend.repository.OrderRepo;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class OrderServiceTest {

    private final OrderRepo mockRepo = mock(OrderRepo.class);
    private final IdService mockUtils = mock(IdService.class);
    private final ProductRepo mockProductRepo = mock(ProductRepo.class);
    private final OrderService service = new OrderService(mockRepo, mockUtils, mockProductRepo);

    @Test
    void addOrder_shouldAddOrder_whenCalledWithOrder() {
        OrderedProduct orderedProduct = new OrderedProduct("1", 22);
        OrderedProduct[] orderedProducts = new OrderedProduct[]{orderedProduct};
        Order expected = new Order("1", orderedProducts, 22, "testuser");
        when(mockUtils.generateUUID()).thenReturn("1");
        when(mockRepo.save(expected)).thenReturn(expected);
        //WHEN
        Order actual = service.addOrder(new OrderDTO(orderedProducts, 22, "testuser"));
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void getAllOrders_shouldReturnEmptyList_whenCalledInitially() {
        //GIVEN
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());
        //WHEN
        List<Order> actual = service.getAllOrders();
        //THEN
        assertEquals(Collections.EMPTY_LIST, actual);
    }

    @Test
    void getOrderById_shouldReturnOrder_whenCalledById() throws InvalidIdException {
        //GIVEN
        OrderedProduct orderedProduct = new OrderedProduct("1", 22);
        OrderedProduct[] orderedProducts = new OrderedProduct[]{orderedProduct};
        Order expected = new Order("1", orderedProducts, 22, "testuser");
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        Order actual = service.getOrderById("1");
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void getOrderById_shouldReturnException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.getOrderById("1"));
        verify(mockRepo).findById("1");
    }

    @Test
    void deleteOrderById_shouldDeleteOrder_whenCalledById() throws InvalidIdException {
        OrderedProduct orderedProduct = new OrderedProduct("1", 22);
        OrderedProduct[] orderedProducts = new OrderedProduct[]{orderedProduct};
        Order expected = new Order("1", orderedProducts, 22, "testuser");
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        when(mockRepo.existsById("1")).thenReturn(true);
        service.deleteOrderById(expected.id());
        verify(mockRepo).deleteById(expected.id());
    }

    @Test
    void deleteOrderById_shouldReturnException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        when(mockRepo.existsById("1")).thenReturn(false);
        assertThrows(InvalidIdException.class, () -> service.deleteOrderById("1"));
        verify(mockRepo).existsById("1");
    }

    @Test
    void getProductsFromOrders_shouldReturnProducts_whenCalledByOrdersId() throws InvalidIdException {
        OrderedProduct orderedProduct = new OrderedProduct("1", 22);
        OrderedProduct[] orderedProducts = new OrderedProduct[]{orderedProduct};
        Order expected = new Order("1", orderedProducts, 22, "testuser");
        Product expectedProduct1 = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0])));
        Product expectedProduct2 = new Product("2", "Tee", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0])));
        Product expectedProduct3 = new Product("3", "Tasse", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0])));
        ArrayList<Product> products = new ArrayList<>();
        products.add(expectedProduct1);
        products.add(expectedProduct2);
        products.add(expectedProduct3);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        List<OrderedProduct> orderedProducts1 = List.of(expected.orderedProducts());
        List<String> productIds = orderedProducts1.stream()
                .map(OrderedProduct::productId)
                .collect(Collectors.toList());
        service.getProductsFromOrders(expected.id());
        verify(mockRepo).findById(expected.id());
        verify(mockProductRepo).findAllById(productIds);
    }

    @Test
    void getProductsFromOrders_shouldThrowException_whenCalledByWrongOrdersId() throws InvalidIdException {
        OrderedProduct orderedProduct = new OrderedProduct("1", 22);
        OrderedProduct[] orderedProducts = new OrderedProduct[]{orderedProduct};
        Order expected = new Order("1", orderedProducts, 22, "testuser");
        Product expectedProduct1 = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0])));
        Product expectedProduct2 = new Product("2", "Tee", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0])));
        Product expectedProduct3 = new Product("3", "Tasse", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0])));
        ArrayList<Product> products = new ArrayList<>();
        products.add(expectedProduct1);
        products.add(expectedProduct2);
        products.add(expectedProduct3);
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.getProductsFromOrders("1"));
        verify(mockRepo).findById("1");
    }


}