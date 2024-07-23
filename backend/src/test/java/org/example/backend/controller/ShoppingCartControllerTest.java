package org.example.backend.controller;

import org.example.backend.model.Order;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.OrderRepo;
import org.example.backend.repository.ShoppingCartRepo;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShoppingCartRepo shoppingCartRepo;

    @BeforeEach
    void setUp() {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        shoppingCartRepo.save(new ShoppingCart("1",productIds));
        shoppingCartRepo.save(new ShoppingCart("2",productIds));
    }
    @Test
    void addShoppingCart_shouldReturnShoppingCart_whenSendWitShoppingCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/shoppingCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
          "productIds": [
            "1",
            "2",
            "3"
          ]
        }
"""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
       {
          "productIds": [
            "1",
            "2",
            "3"
          ]
        }
"""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
    @Test
    void getAllShoppingCarts_shouldReturnAllShoppingCarts_whenCalledInitially() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shoppingCart"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
[
    {
        "id": "1",
        "productIds": [
            "1",
            "2",
            "3"
        ]
    },
    {
        "id": "2",
        "productIds": [
            "1",
            "2",
            "3"
        ]
    }
]
"""));
    }
    @Test
    void getShoppingCartById_shouldReturnShoppingCart_whenCalledById() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shoppingCart/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
    {
        "id": "1",
        "productIds": [
            "1",
            "2",
            "3"
        ]
    }
"""));}
    @Test
    void updateShoppingCart_shouldAddProductToShoppingCart_whenCalledByShoppingCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/shoppingCart/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
    {
        "productIds": [
            "1",
            "2",
            "3"
        ]
    }
"""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
    {
        "id": "2",
        "productIds": [
            "1",
            "2",
            "3"
        ]
    }
"""));
    }
}