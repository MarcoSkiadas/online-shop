package org.example.backend.service;

import org.example.backend.dto.ProductDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.*;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private final ProductRepo mockRepo = mock(ProductRepo.class);
    private final IdService mockUtils = mock(IdService.class);
    private final CloudinaryService mockCloud = mock(CloudinaryService.class);
    private final ProductService service = new ProductService(mockRepo, mockUtils, mockCloud);
    private final MultipartFile multipartFile = mock(MultipartFile.class);

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
        Product expected = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test");
        when(mockRepo.findById("1")).thenReturn(Optional.of(expected));
        //WHEN
        Product actual = service.getProductById("1");
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void getProductById_shouldReturnException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.getProductById("1"));
        verify(mockRepo).findById("1");
    }

    @Test
    void deleteProduct_shouldDeleteProduct_whenCalledById() throws InvalidIdException {
        String productId = "123";
        when(mockRepo.existsById(productId)).thenReturn(true);
        service.deleteProduct(productId);
        verify(mockRepo).deleteById(productId);
    }

    @Test
    void deleteProduct_shouldThrowException_whenCalledByWrongId() throws InvalidIdException {
        String productId = "123";
        when(mockRepo.existsById(productId)).thenReturn(false);
        assertThrows(InvalidIdException.class, () -> service.deleteProduct("1"));
        verify(mockRepo).existsById("1");
    }

    @Test
    void getAllProductsByIds_shouldReturnProducts_whenCalledByOrdersId() throws InvalidIdException {
        List<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        Product expectedProduct1 = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test");
        Product expectedProduct2 = new Product("2", "Tee", 22, new Quantity(2, Unit.PIECE), "Test");
        Product expectedProduct3 = new Product("3", "Tasse", 22, new Quantity(2, Unit.PIECE), "Test");
        ArrayList<Product> products = new ArrayList<>();
        products.add(expectedProduct1);
        products.add(expectedProduct2);
        products.add(expectedProduct3);
        when(mockRepo.findAllById(productIds)).thenReturn(products);
        service.getAllProductsByIds(productIds);
        verify(mockRepo).findAllById(productIds);
    }

    @Test
    void reduceProductOnStock_shouldReduceProductOnStock_whenCalledByIdAndAmount() throws InvalidIdException {
        Product expectedProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test");
        Product actualProduct = new Product("1", "Rasenmäher", 22, new Quantity(4, Unit.PIECE), "Test");
        when(mockRepo.findById("1")).thenReturn(Optional.of(actualProduct));
        when(mockRepo.save(expectedProduct)).thenReturn(expectedProduct);
        actualProduct = service.reduceProductOnStock("1", 2);
        assertEquals(expectedProduct, actualProduct);
        verify(mockRepo).findById("1");
        verify(mockRepo).save(expectedProduct);
    }

    @Test
    void reduceProductOnStock_shouldReturnException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.reduceProductOnStock("1", 2));
        verify(mockRepo).findById("1");
        verify(mockRepo, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnProduct_WhenCalledWithImage() throws IOException {
        String productId = "1";
        String imageUrl = "http://example.com/image.jpg";
        ProductDTO productDTO = new ProductDTO("Rasenmäher", 22, new Quantity(2, Unit.PIECE));
        Product existingProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "old-image-url");
        Product updatedProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), imageUrl);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(mockCloud.uploadImage(multipartFile)).thenReturn(imageUrl);
        when(mockRepo.existsById(productId)).thenReturn(true);
        when(mockRepo.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(mockRepo.save(updatedProduct)).thenReturn(updatedProduct);

        Product actualProduct = service.updateProduct(multipartFile, productId, productDTO);

        assertEquals(updatedProduct, actualProduct);

        verify(mockCloud).uploadImage(multipartFile);
        verify(mockRepo).findById(productId);
        verify(mockRepo).save(updatedProduct);
    }

    @Test
    void updateProduct_WhenFileIsEmpty() throws IOException {
        String productId = "1";
        String imageUrl = "old-image-url";
        ProductDTO productDTO = new ProductDTO("Rasenmäher", 22, new Quantity(2, Unit.PIECE));
        Product existingProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), imageUrl);
        Product updatedProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), imageUrl);

        when(multipartFile.isEmpty()).thenReturn(true);

        when(mockRepo.existsById(productId)).thenReturn(true);
        when(mockRepo.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(mockRepo.save(any(Product.class))).thenReturn(updatedProduct);


        Product actualProduct = service.updateProduct(multipartFile, productId, productDTO);


        assertEquals(updatedProduct, actualProduct);

        verify(mockCloud, never()).uploadImage(multipartFile);
        verify(mockRepo).findById(productId);
        verify(mockRepo).save(updatedProduct);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() throws IOException {
        String productId = "1";
        String imageUrl = "https://example.com/image.jpg";
        ProductDTO productDTO = new ProductDTO("Rasenmäher", 22, new Quantity(2, Unit.PIECE));

        when(multipartFile.isEmpty()).thenReturn(false);
        when(mockCloud.uploadImage(multipartFile)).thenReturn(imageUrl);
        when(mockRepo.existsById(productId)).thenReturn(false);

        assertThrows(InvalidIdException.class, () -> {
            service.updateProduct(multipartFile, productId, productDTO);
        });

        verify(mockCloud, never()).uploadImage(multipartFile);
        verify(mockRepo, never()).findById(productId);
        verify(mockRepo, never()).save(any(Product.class));
    }

    @Test
    void testAddProductWithEmptyMultipartFile() throws IOException {

        when(multipartFile.isEmpty()).thenReturn(true);
        ProductDTO productDTO = new ProductDTO("Rasenmäher", 22, new Quantity(2, Unit.PIECE));
        String generatedId = "123-uuid";
        String defaultImageUrl = "http://res.cloudinary.com/dylxokrcs/image/upload/v1722871122/jtc7ycksrhoo5larwkyw.jpg";
        Product expectedProduct = new Product(generatedId, productDTO.name(), productDTO.price(), productDTO.quantity(), defaultImageUrl);

        when(mockUtils.generateUUID()).thenReturn(generatedId);
        when(mockRepo.save(any(Product.class))).thenReturn(expectedProduct);

        Product actualProduct = service.addProduct(multipartFile, productDTO);

        assertEquals(expectedProduct, actualProduct);
        verify(mockUtils).generateUUID();
        verify(mockRepo, times(2)).save(any(Product.class));
        verify(mockCloud, never()).uploadImage(any());
    }

    @Test
    void testAddProductWithValidMultipartFile() throws IOException {

        String imageUrl = "old-image-url";
        when(multipartFile.isEmpty()).thenReturn(false);
        byte[] content = "image content".getBytes();
        ProductDTO productDTO = new ProductDTO("Rasenmäher", 22, new Quantity(2, Unit.PIECE));
        String generatedId = "123-uuid";
        String uploadedImageUrl = "http://res.cloudinary.com/somepath/image.jpg";
        Product expectedProduct = new Product(generatedId, productDTO.name(), productDTO.price(), productDTO.quantity(), uploadedImageUrl);

        when(mockUtils.generateUUID()).thenReturn(generatedId);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(mockCloud.uploadImage(multipartFile)).thenReturn(imageUrl);
        when(mockRepo.save(any(Product.class))).thenReturn(expectedProduct);

        Product actualProduct = service.addProduct(multipartFile, productDTO);

        assertEquals(expectedProduct, actualProduct);
        verify(mockUtils, times(1)).generateUUID();
        verify(mockCloud, times(1)).uploadImage(multipartFile);
        verify(mockRepo, times(2)).save(any(Product.class));
    }
}