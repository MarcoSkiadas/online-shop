package org.example.backend.controller;

import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {

   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private MockMvc mockMvc;

    @Test
    void getAllOrders_shouldReturnAllProducts_whenCalledInitially() throws Exception {

        productRepo.save(new Product("1","Rasenmäher",22));
        productRepo.save(new Product("2","Tasse",22));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
[
    {
        "id": "1",
        "name": "Rasenmäher",
        "price": 22
    },
    {
        "id": "2",
        "name": "Tasse",
        "price": 22
    }
    ]
"""));
    }

}