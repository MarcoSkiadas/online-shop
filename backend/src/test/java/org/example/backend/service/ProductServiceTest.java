package org.example.backend.service;

import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

private final ProductRepo mockRepo = mock(ProductRepo.class);
private final IdService mockUtil = mock(IdService.class);

    @Test
    void GetAllProducts_shouldReturnEmptyList_whenCalledInitially() {
        ProductService service = new ProductService(mockRepo,mockUtil);
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());
        List<Product> actual = service.getAllProducts();
        assertEquals(Collections.EMPTY_LIST, actual);


    }
}