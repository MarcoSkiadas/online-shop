package org.example.backend.service;

import org.example.backend.dto.AppUserDTO;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.*;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    private final IdService mockIdService = mock(IdService.class);
    private final AppUserService service = new AppUserService(mockRepo, mockProductRepo, mockIdService);


    @BeforeEach
    void setUp() {
        mockProductRepo.save(new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), new Images("largeTest", "smallTest"), 0, new ArrayList<>(List.of(new Review[0]))));
    }

    @Test
    void addProductToShoppingCart_shouldAddProductToShoppingCart_whenCalledById() throws InvalidIdException {
        String userId = "1";
        String productId = "1";
        AppUser testAppUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[0]));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockProductRepo.existsById(productId)).thenReturn(true);
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.addProductToShoppingCart(userId, productId, 2);
        AppUser expectedUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 2)}));
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
    void addProductToShoppingCart_shouldThrowException_whenCalledByIdAlreadyExist() throws InvalidIdException {
        String userId = "1";
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        AppUser testAppUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 3), new OrderedProduct("2", 3)}));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockProductRepo.existsById("1")).thenReturn(true);
        assertThrows(InvalidIdException.class, () -> service.addProductToShoppingCart("1", "1", 2));
        verify(mockRepo).findById("1");
        verify(mockProductRepo).existsById("1");
    }

    @Test
    void addProductToShoppingCart_shouldThrowException_whenCalledByIdWhichDoesntExist() throws InvalidIdException {
        String userId = "1";
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add("1");
        productIds.add("2");
        productIds.add("3");
        AppUser testAppUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 3), new OrderedProduct("2", 3)}));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockProductRepo.existsById("1")).thenReturn(false);
        assertThrows(InvalidIdException.class, () -> service.addProductToShoppingCart("1", "1", 2));
        verify(mockRepo).findById("1");
        verify(mockProductRepo).existsById("1");
    }


    @Test
    void removeProductFromShoppingCart_shouldRemoveProductFromShoppingCart_whenCalledById() throws InvalidIdException {
        String userId = "1";
        String productId = "1";
        AppUser testAppUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 2)}));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.removeProductFromShoppingCart(userId, productId);
        AppUser expectedUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[0]));
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
        AppUser testAppUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(orderedProducts1));
        when(mockRepo.findById(userId)).thenReturn(Optional.of(testAppUser));
        when(mockRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AppUser actualUser = service.removeAllProductsFromShoppingCart(userId);
        AppUser expectedUser = new AppUser(userId, "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[0]));
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

    @Test
    void loadUserByUsername_shouldReturnUser_whenCalled() throws UsernameNotFoundException {
        AppUser testAppUser = new AppUser("1", "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[]{new OrderedProduct("1", 2)}));
        mockRepo.save(testAppUser);
        UserDetails expectedUser = new User(testAppUser.username(), testAppUser.password(), Collections.emptyList());
        when(mockRepo.findByUsername("TestUser")).thenReturn(Optional.of(testAppUser));
        UserDetails actualUser = service.loadUserByUsername("TestUser");
        service.loadUserByUsername("TestUser");
        assertEquals(expectedUser, actualUser);
        verify(mockRepo, times(2)).findByUsername("TestUser");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenCalledWithWrongUser() throws UsernameNotFoundException {
        when(mockRepo.findByUsername("TestUser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("TestUser"));
        verify(mockRepo).findByUsername("TestUser");
    }

    @Test
    void getUserByUsername_shouldReturnUser_whenCalled() throws UsernameNotFoundException {
        AppUser testAppUser = new AppUser("1", "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[0]));
        mockRepo.save(testAppUser);
        when(mockRepo.findByUsername("TestUser")).thenReturn(Optional.of(testAppUser));
        AppUser actualUser = service.getUserByUsername("TestUser");
        service.loadUserByUsername("TestUser");
        assertEquals(testAppUser, actualUser);
        verify(mockRepo, times(2)).findByUsername("TestUser");
    }

    @Test
    void getUserByUsername_shouldThrowException_whenCalledWithWrongUser() throws UsernameNotFoundException {
        when(mockRepo.findByUsername("TestUser")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.getUserByUsername("TestUser"));
        verify(mockRepo).findByUsername("TestUser");
    }

    @Test
    void registerNewUser_shouldReturnUser_whenCalled() throws InvalidIdException {
        AppUser testAppUser = new AppUser("1", "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[0]));
        mockRepo.save(testAppUser);
        service.registerNewUser(new AppUserDTO("TestUser", "swordfish"));
        verify(mockRepo).save(testAppUser);
    }

    @Test
    void registerNewUser_shouldReturnException_whenCalledWithTakenUsername() throws InvalidIdException {
        AppUser testAppUser = new AppUser("1", "TestUser", "swordfish", "USER", new ShoppingCart(new OrderedProduct[0]));
        mockRepo.save(testAppUser);
        when(mockRepo.findByUsername("TestUser")).thenReturn(Optional.of(testAppUser));
        assertThrows(InvalidIdException.class, () -> service.registerNewUser(new AppUserDTO("TestUser", "swordfish")));
        verify(mockRepo, times(1)).save(testAppUser);
    }


}