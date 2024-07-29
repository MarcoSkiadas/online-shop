package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.OrderDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Order;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.OrderRepo;
import org.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final IdService idService;
    private final ProductRepo productRepo;


    public Order addOrder(OrderDTO order) {

        String id = idService.generateUUID();
        Order o = new Order(id, order.productIds(), order.price(), order.userId());
        return orderRepo.save(o);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(String id) throws InvalidIdException {
        Optional<Order> order = orderRepo.findById(id);
        return order.orElseThrow(() -> new InvalidIdException("Order with " + id + " not found"));
    }

    public void deleteOrderById(String id) throws InvalidIdException {
        if (orderRepo.existsById(id)) {
            orderRepo.deleteById(id);
        } else {
            throw new InvalidIdException("Order with " + id + " not found");
        }
    }

    public List<Product> getProductsFromOrders(String id) throws InvalidIdException {
        Order order = orderRepo.findById(id).orElseThrow(() -> new InvalidIdException("Order with " + id + " not found"));
        return productRepo.findAllById(order.productIds());
    }
}
