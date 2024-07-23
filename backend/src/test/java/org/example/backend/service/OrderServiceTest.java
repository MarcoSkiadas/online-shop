package org.example.backend.service;

import org.example.backend.dto.OrderDTO;
import org.example.backend.model.Order;
import org.example.backend.repository.OrderRepo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class OrderServiceTest {

    private final OrderRepo mockRepo = mock(OrderRepo.class);
    private final IdService mockUtils = mock(IdService.class);
    private final OrderService service = new OrderService(mockRepo,mockUtils);

    @Test
    void addOrder_shouldAddOrder_whenCalledWithOrder() {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        Order expected = new Order("1",productIds,22);
        when(mockUtils.generateUUID()).thenReturn("1");
        when(mockRepo.save(expected)).thenReturn(expected);
        //WHEN
        Order actual = service.addOrder(new OrderDTO(productIds,22));
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
    void getOrderById_shouldReturnOrder_whenCalledById() {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        Order expected = new Order("1",productIds,22);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        Order actual = service.getOrderById("1");
        //THEN
        assertEquals(expected, actual);
    }
    @Test
    void deleteOrderById_shouldDeleteOrder_whenCalledById() {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        Order expected = new Order("1",productIds,22);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        service.deleteOrderById(expected.id());
        verify(mockRepo).deleteById(expected.id());
    }

}