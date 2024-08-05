package org.example.backend.service;

import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.*;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        mockProductRepo.save(new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test"));
    }

    @Test
    void addProductToShoppingCart_shouldAddProductToShoppingCart_whenCalledById() throws InvalidIdException {
        String userId = "1";
        String productId = "1";
        AppUser testAppUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(new OrderedProduct[0]));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockProductRepo.existsById(productId)).thenReturn(true);
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.addProductToShoppingCart(userId, productId, 2);
        AppUser expectedUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 2)}));
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
        assertThrows(InvalidIdException.class, () -> service.addProductToShoppingCart("1", "1", 2));
        verify(mockRepo).findById("1");
    }

    @Test
    void removeProductFromShoppingCart_shouldRemoveProductFromShoppingCart_whenCalledById() throws InvalidIdException {
        String userId = "1";
        String productId = "1";
        AppUser testAppUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 2)}));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.removeProductFromShoppingCart(userId, productId);
        AppUser expectedUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(new OrderedProduct[0]));
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

    @Test
    void removeAllProductsFromShoppingCart_shouldReturnEmptyList_whenCalled() throws InvalidIdException {
        String userId = "1";
        OrderedProduct orderedProduct1 = new OrderedProduct("1", 22);
        OrderedProduct[] orderedProducts1 = new OrderedProduct[]{orderedProduct1};
        AppUser testAppUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(orderedProducts1));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.removeAllProductsFromShoppingCart(userId);
        AppUser expectedUser = new AppUser(userId, "TestUser", "USER", new ShoppingCart(new OrderedProduct[0]));
        assertEquals(expectedUser, actualUser);
        verify(mockRepo).findById(userId);
        verify(mockRepo).save(any(AppUser.class));
    }

    @Test
    void removeAllProductsFromShoppingCart_shouldThrowException_whenCalledByWrongId() throws InvalidIdException {
        when(mockRepo.findById("1")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> service.removeAllProductsFromShoppingCart("1"));
        verify(mockRepo).findById("1");
        verify(mockRepo, never()).save(any(AppUser.class));
    }
}