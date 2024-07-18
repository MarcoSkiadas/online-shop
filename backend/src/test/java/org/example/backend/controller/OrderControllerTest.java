package org.example.backend.controller;

import org.example.backend.model.Order;
import org.example.backend.repository.OrderRepo;
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
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepo orderRepo;

    @Test
    void addOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
          "productIds": [
            "1",
            "2",
            "3"
          ],
          "price": 55
        }
"""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
       {
          "productIds": [
            "1",
            "2",
            "3"
          ],
          "price": 55
        }
"""));
    }
    @Test
    void getAllOrders_shouldReturnAllOrders_whenCalledInitially() throws Exception {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        orderRepo.save(new Order("1",productIds,22));
        orderRepo.save(new Order("2",productIds,22));
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
[
    {
        "id": "1",
        "productIds": [
            "1",
            "2",
            "3"
        ],
        "price": 22
    },
    {
        "id": "2",
        "productIds": [
            "1",
            "2",
            "3"
        ],
        "price": 22
    }
]
"""));
    }
    @Test
    void getOrderById_shouldReturnOrder_whenCalledById() throws Exception {
        //GIVEN
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        orderRepo.save(new Order("1",productIds,22));
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
    {
        "id": "1",
        "productIds": [
            "1",
            "2",
            "3"
        ],
        "price": 22
    }
"""));}
}