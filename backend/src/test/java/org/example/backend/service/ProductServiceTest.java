package org.example.backend.service;

import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

private final ProductRepo mockRepo = mock(ProductRepo.class);
private final IdService mockUtil = mock(IdService.class);
private final ProductService service = new ProductService(mockRepo,mockUtil);

    @Test
    void getAllProducts_shouldReturnEmptyList_whenCalledInitially() {
        //GIVEN
        when(mockRepo.findAll()).thenReturn(Collections.emptyList());
        //WHEN
        List<Product> actual = service.getAllProducts();
        //THEN
        assertEquals(Collections.EMPTY_LIST, actual);


    }
    @Test
    void getProductById_shouldReturnProduct_whenCalledById() {
        //GIVEN
        Product expected = new Product("1","Rasebnäher",22);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        Product actual = service.getProductById("1");
        //THEN
        assertEquals(expected, actual);
    }
}