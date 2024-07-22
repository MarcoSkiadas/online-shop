package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.OrderDTO;
import org.example.backend.model.Order;
import org.example.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public Order addOrder(@RequestBody OrderDTO order) {
        return orderService.addOrder(order);
    }

    @GetMapping()
    public List<Order> getAllOrders() { return orderService.getAllOrders(); }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id){ return orderService.getOrderById(id);}

    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable String id) {orderService.deleteOrderById(id);}

}
