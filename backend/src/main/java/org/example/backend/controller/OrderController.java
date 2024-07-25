package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.OrderDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Order;
import org.example.backend.model.Product;
import org.example.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Order addOrder(@RequestBody OrderDTO order) {
        return orderService.addOrder(order);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) throws InvalidIdException {
        return orderService.getOrderById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable String id) throws InvalidIdException {
        orderService.deleteOrderById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/products")
    public List<Product> getProductsFromOrders(@PathVariable String id) throws InvalidIdException {
        return orderService.getProductsFromOrders(id);
    }

}
