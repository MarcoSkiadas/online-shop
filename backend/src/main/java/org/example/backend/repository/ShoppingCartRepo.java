package org.example.backend.repository;

import org.example.backend.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepo extends MongoRepository<ShoppingCart, String> {
}
