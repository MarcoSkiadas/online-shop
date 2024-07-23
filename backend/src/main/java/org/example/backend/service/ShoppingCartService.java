package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.ShoppingCartRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepo shoppingCartRepo;
    private final IdService idService;


    public ShoppingCart addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        String id = idService.generateUUID();
        ShoppingCart s = new ShoppingCart(id,shoppingCartDTO.productIds());
        return shoppingCartRepo.save(s);
    }
}
