package org.example.backend.service;

import org.example.backend.dto.ShoppingCartDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.AppUser;
import org.example.backend.model.Product;
import org.example.backend.model.ShoppingCart;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.repository.ProductRepo;
import org.example.backend.repository.ShoppingCartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class AppUserServiceTest {

    private final AppUserRepository mockRepo = mock(AppUserRepository.class);
    private final ProductRepo mockProductRepo = mock(ProductRepo.class);
    private final AppUserService service = new AppUserService(mockRepo, mockProductRepo);

    @BeforeEach
    void setUp() {
        mockProductRepo.save(new Product("1", "Rasenmäher", 22));
    }

    @Test
    void addProductToShoppingCart_shouldAddProductToShoppingCart_whenCalledById() throws InvalidIdException {
        String userId = "1";
        String productId = "1";
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("2");
        productIds.add("3");
        AppUser testAppUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(productIds));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockProductRepo.existsById(productId)).thenReturn(true);
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.addProductToShoppingCart(userId, productId);
        ArrayList<String> expectedProductIds = new ArrayList<>();
        expectedProductIds.add("2");
        expectedProductIds.add("3");
        expectedProductIds.add(productId);
        AppUser expectedUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(expectedProductIds));
        assertEquals(expectedUser, actualUser);
        verify(mockRepo).findById(userId);
        verify(mockProductRepo).existsById(productId);
        verify(mockRepo).save(any(AppUser.class));
    }


    @Test
    void addProductToShoppingCart_shouldThrowException_whenCalledByWrongId() throws InvalidIdException {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        when(mockRepo.existsById("1")).thenReturn(false);
        assertThrows(InvalidIdException.class, () -> service.addProductToShoppingCart("1", "1"));
        verify(mockRepo).findById("1");
    }

    @Test
    void removeProductFromShoppingCart_shouldRemoveProductFromShoppingCart_whenCalledById() throws InvalidIdException {
        String userId = "1";
        String productId = "1";
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        AppUser testAppUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(productIds));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.removeProductFromShoppingCart(userId, productId);
        ArrayList<String> expectedProductIds = new ArrayList<>(productIds);
        expectedProductIds.remove(productId);
        AppUser expectedUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(expectedProductIds));
        assertEquals(expectedUser, actualUser);
        verify(mockRepo).findById(userId);
        verify(mockRepo).save(any(AppUser.class));
    }

    @Test
    void removeProductToShoppingCart_shouldThrowException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.removeProductFromShoppingCart("1", "2"));
        verify(mockRepo).findById("1");
        verify(mockRepo, never()).save(any(AppUser.class));
    }


}