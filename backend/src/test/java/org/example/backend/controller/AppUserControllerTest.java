package org.example.backend.controller;

import org.example.backend.model.AppUser;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
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
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        appUserRepository.save(new AppUser("1", "testuser", "USER", new ShoppingCart(productIds)));
        appUserRepository.save(new AppUser("2", "testuser", "USER", new ShoppingCart(productIds)));
        productRepo.save(new Product("1", "Rasenmäher", 22));
        productRepo.save(new Product("2", "Tee", 22));
        productRepo.save(new Product("3", "Tasse", 22));
        productRepo.save(new Product("4", "Teekanne", 22));
    }

    @Test
    void addProductToShoppingCart_shouldAddProductToShoppingCart_whenCalledByShoppingCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/appuser/shoppingCart/addProduct/1/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                {
                                                    "id": "1",
                                                    "username": "testuser",
                                                    "role": "USER",
                                                    "shoppingCart": {
                                                        "productIds": [
                                                            "1",
                                                            "2",
                                                            "3",
                                                            "4"
                                                        ]
                                                    }
                                                }
                        """));
    }

    @Test
    void addProductToShoppingCart_shouldReturnException_whenCalledByWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/appuser/shoppingCart/addProduct/3/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                              "apiPath": "uri=/api/appuser/shoppingCart/addProduct/3/1",
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
                                                        "productIds": [
                                                        "2",
                                                        "3"
                                                        ]
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