package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.OrderDTO;
import org.example.backend.model.Order;
import org.example.backend.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(OrderDTO order) {

        String id = idService.generateUUID();
        Order o = new Order(id,order.productIds(), order.price());
        return orderRepo.save(o);
    };

}
