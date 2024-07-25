package org.example.backend.controller;

import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.example.backend.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        productRepo.save(new Product("1", "Rasenmäher", 22));
        productRepo.save(new Product("2", "Tasse", 22));
    }

    @Test
    void getAllProducts_shouldReturnAllProducts_whenCalledInitially() throws Exception {
        //WHEN & THEN
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

    @Test
    void getProductById_shouldReturnProduct_whenCalledById() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "id": "2",
                                "name": "Tasse",
                                "price": 22
                            }
                        """));
    }

    @Test
    void getProductById_shouldReturnException_whenCalledByWrongId() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                              "apiPath": "uri=/api/product/3",
                              "errorCode": "NOT_FOUND",
                              "errorMsg": "Product with 3 not found"
                            }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime").isNotEmpty());
    }

    @Test
    void updateProduct_shouldUpdateProduct_whenCalledByProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/product/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Rasenmäher",
                                    "price": 19
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "id": "2",
                                "name": "Rasenmäher",
                                "price": 19
                            }
                        """));
    }

    @Test
    void deleteProduct_shouldReturnIsOk_whenPerformed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteProduct_shouldReturnException_whenCalledByWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "apiPath": "uri=/api/product/3",
                                "errorCode": "NOT_FOUND",
                                "errorMsg": "Product with 3 not found"
                            }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime").isNotEmpty());
    }

    @Test
    void addProduct_shouldReturnProduct_whenSendWithProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                        "name": "Rasenmäher",
                                        "price": 19
                                    }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                               {
                                "name": "Rasenmäher",
                                "price": 19
                            }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
}