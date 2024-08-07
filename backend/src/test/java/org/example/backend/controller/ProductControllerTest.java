package org.example.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.example.backend.dto.ProductDTO;
import org.example.backend.model.Product;
import org.example.backend.model.Quantity;
import org.example.backend.model.Review;
import org.example.backend.model.Unit;
import org.example.backend.repository.ProductRepo;
import org.example.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {

    @MockBean
    Cloudinary cloudinary;
    Uploader uploader = mock(Uploader.class);
    ProductService productService = mock(ProductService.class);
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        productRepo.save(new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>(List.of(new Review[0]))));
        productRepo.save(new Product("2", "Tasse", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>(List.of(new Review[0]))));
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
    void getAllProductsByIds_shouldReturnProducts_whenCalledByOrderId() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/shoppingCart?productIds=1&productIds=2"))
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
    void reduceProductOnStock_shouldReturnException_whenCalledByWrongId() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/product/shoppingCart/3/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                              "apiPath": "uri=/api/product/shoppingCart/3/1",
                              "errorCode": "NOT_FOUND",
                              "errorMsg": "Product with 3 not found"
                            }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime").isNotEmpty());
    }

    @Test
    void reduceProductOnStock_shouldReturnProduct_whenCalledByIdAndAmount() throws Exception {
        //WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/product/shoppingCart/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                    {
                                                        "id": "1",
                                                        "name": "Rasenmäher",
                                                        "price": 22,
                                                        "quantity": {
                                                              "amount": 1,
                                                              "unit": "PIECE"
                                                                    }
                                                    }
                        """));
    }

    @Test
    void addProduct_shouldReturnProduct_whenAddedWithImage() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenReturn(Map.of("url", "testurl"));

        MockMultipartFile mockFile = new MockMultipartFile("file", "content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, """
                {
                  "name": "Rasenmäher",
                  "price": 22,
                  "quantity": {
                    "amount": 2,
                    "unit": "PIECE"
                  }
                }
                """.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/product/upload")
                        .file(mockFile)
                        .file(mockJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                                                                           "name": "Rasenmäher",
                                                                           "price": 22,
                                                                           "quantity": {
                                                                               "amount": 2,
                                                                               "unit": "PIECE"
                                                                           },
                                                                           "imageUrl": "testurl",
                                                                           "rating": 0.0,
                                                                           "reviewList": []
                                                                       }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void addProduct_shouldReturnProduct_whenAddedWithoutImage() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenReturn(Map.of("url", "testurl"));

        MockMultipartFile mockFile = new MockMultipartFile("file", new byte[0]);
        MockMultipartFile mockJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, """
                {
                  "name": "Rasenmäher",
                  "price": 22,
                  "quantity": {
                    "amount": 2,
                    "unit": "PIECE"
                  }
                }
                """.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/product/upload")
                        .file(mockFile)
                        .file(mockJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                                                                           "name": "Rasenmäher",
                                                                           "price": 22,
                                                                           "quantity": {
                                                                               "amount": 2,
                                                                               "unit": "PIECE"
                                                                           },
                                                                           "imageUrl": "http://res.cloudinary.com/dylxokrcs/image/upload/v1722871122/jtc7ycksrhoo5larwkyw.jpg",
                                                                           "rating": 0.0,
                                                                           "reviewList": []
                                                                       }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void UpdateProduct_shouldReturnProduct_whenAddedWithoutImage() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenReturn(Map.of("url", "testurl"));

        MockMultipartFile mockFile = new MockMultipartFile("file", new byte[0]);
        MockMultipartFile mockJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, """
                {
                  "name": "Rasenmäher",
                  "price": 22,
                  "quantity": {
                    "amount": 2,
                    "unit": "PIECE"
                  }
                }
                """.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/product/upload/1")
                        .file(mockFile)
                        .file(mockJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                                                                           "name": "Rasenmäher",
                                                                           "price": 22,
                                                                           "quantity": {
                                                                               "amount": 2,
                                                                               "unit": "PIECE"
                                                                           },
                                                                           "imageUrl": "Test",
                                                                           "rating": 0.0,
                                                                           "reviewList": []
                                                                       }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void UpdateProduct_shouldReturnProduct_whenAddedWithImage() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenReturn(Map.of("url", "testurl"));

        MockMultipartFile mockFile = new MockMultipartFile("file", "content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockJson = new MockMultipartFile("product", "", MediaType.APPLICATION_JSON_VALUE, """
                {
                  "name": "Rasenmäher",
                  "price": 22,
                  "quantity": {
                    "amount": 2,
                    "unit": "PIECE"
                  }
                }
                """.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/product/upload/1")
                        .file(mockFile)
                        .file(mockJson)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                                                                           "name": "Rasenmäher",
                                                                           "price": 22,
                                                                           "quantity": {
                                                                               "amount": 2,
                                                                               "unit": "PIECE"
                                                                           },
                                                                           "imageUrl": "testurl",
                                                                           "rating": 0.0,
                                                                           "reviewList": []
                                                                       }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void rateProduct_shouldReturnProduct_whenAddedNewReview() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product/1/rate")
                        .param("newRating", "5")
                        .param("commentary", "Great!")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                    {
                                                        "id": "1",
                                                        "name": "Rasenmäher",
                                                        "price": 22,
                                                        "quantity": {
                                                              "amount": 2,
                                                              "unit": "PIECE"
                                                                    },
                                                                           "imageUrl": "Test",
                                                                           "rating": 5.0,
                                                                           "reviewList": [        {
                                                                                                      "ratingCount": 5.0,
                                                                                                      "commentary": "Great!"
                                                                                                  }
                                                                                          ]
                                                    }
                        """));
    }

    @Test
    void rateProduct_shouldReturnException_whenAddedWithWrongProductId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product/3/rate")
                        .param("newRating", "5")
                        .param("commentary", "Great!")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "apiPath": "uri=/api/product/3/rate",
                            "errorCode": "NOT_FOUND",
                            "errorMsg": "Product with 3 not found"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorTime").isNotEmpty());
    }

}