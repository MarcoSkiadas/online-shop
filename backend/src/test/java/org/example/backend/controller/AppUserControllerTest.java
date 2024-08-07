package org.example.backend.controller;

import org.example.backend.model.*;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.repository.ProductRepo;
import org.example.backend.repository.ShoppingCartRepo;
import org.example.backend.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AppUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        OrderedProduct orderedProduct = new OrderedProduct("1", 2);
        appUserRepository.save(new AppUser("1", "testuser", "USER", new ShoppingCart(new OrderedProduct[]{orderedProduct})));
        appUserRepository.save(new AppUser("2", "testuser", "USER", new ShoppingCart(new OrderedProduct[]{orderedProduct})));
        productRepo.save(new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>(List.of(new Review[0]))));
        productRepo.save(new Product("2", "Tee", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>(List.of(new Review[0]))));
        productRepo.save(new Product("3", "Tasse", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>(List.of(new Review[0]))));
        productRepo.save(new Product("4", "Teekanne", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>(List.of(new Review[0]))));
    }

    @Test
    void addProductToShoppingCart_shouldAddProductToShoppingCart_whenCalledByShoppingCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/appuser/shoppingCart/addProduct/1/4/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                {
                                                    "id": "1",
                                                    "username": "testuser",
                                                    "role": "USER",
                                                    "shoppingCart": {
                                                                 "orderedProducts": [
                                                                     {
                                                                         "productId": "4",
                                                                         "amount": 3
                                                                     },
                                                                     {
                                                                         "productId": "1",
                                                                         "amount": 2
                                                                     }
                                                                 ]
                                                    }
                                                }
                        """));
    }

    @Test
    void addProductToShoppingCart_shouldReturnException_whenCalledByWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/appuser/shoppingCart/addProduct/3/4/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                              "apiPath": "uri=/api/appuser/shoppingCart/addProduct/3/4/3",
                              "errorCode": "NOT_FOUND",
                              "errorMsg": "User with 3 not found"
                            }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime").isNotEmpty());
    }


    @Test
    void removeProductToShoppingCart_shouldRemoveProductToShoppingCart_whenCalledByShoppingCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/appuser/shoppingCart/removeProduct/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                {
                                                    "id": "1",
                                                    "username": "testuser",
                                                    "role": "USER",
                                                    "shoppingCart": {
                                                        "orderedProducts": []
                                                    }
                                                }
                        """));
    }

    @Test
    void removeProductToShoppingCart_shouldReturnException_whenCalledByWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/appuser/shoppingCart/removeProduct/1/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                              "apiPath": "uri=/api/appuser/shoppingCart/removeProduct/1/4",
                              "errorCode": "NOT_FOUND",
                              "errorMsg": "Product with 4 not found in Shopping Cart"
                            }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime").isNotEmpty());
    }


}