package org.example.backend.service;

import org.example.backend.dto.OrderDTO;
import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.Order;
import org.example.backend.model.Product;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private final ProductRepo mockRepo = mock(ProductRepo.class);
    private final IdService mockUtils = mock(IdService.class);
    private final ProductService service = new ProductService(mockRepo, mockUtils);

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
    void getProductById_shouldReturnProduct_whenCalledById() throws InvalidIdException {
        //GIVEN
        Product expected = new Product("1", "Rasenmäher", 22);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        Product actual = service.getProductById("1");
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void updateProduct_shouldUpdateProduct_whenCalledById() throws InvalidIdException {
        //GIVEN
        Product expected = new Product("1", "Rasenmäher", 22);
        Product actual = new Product("1", "Rasenmäher", 44);
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        when(mockRepo.save(expected)).thenReturn(expected);
        ProductDTO expectedDTO = new ProductDTO("Rasenmäher", 22);
        //WHEN
        actual = service.updateProduct("1", expectedDTO);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void deleteProduct_shouldDeleteProduct_whenCalledById() throws InvalidIdException {
        String productId = "123";
        when(mockRepo.existsById(productId)).thenReturn(true);
        service.deleteProduct(productId);
        verify(mockRepo).deleteById(productId);
    }

    @Test
    void addOrder_shouldAddOrder_whenCalledWithOrder() {
        Product expected = new Product("1", "Rasenmäher", 22);
        when(mockUtils.generateUUID()).thenReturn("1");
        when(mockRepo.save(expected)).thenReturn(expected);
        //WHEN
        Product actual = service.addProduct(new ProductDTO("Rasenmäher", 22));
        //THEN
        assertEquals(expected, actual);
        verify(mockRepo).save(expected);
        verify(mockUtils).generateUUID();
    }

}