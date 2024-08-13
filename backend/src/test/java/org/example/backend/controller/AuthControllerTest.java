package org.example.backend.controller;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.backend.model.*;
import org.example.backend.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {


    @Autowired
    private AppUserRepository appUserRepo;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        appUserRepo.save(new AppUser("1", "testUser", "", "USER", new ShoppingCart(new OrderedProduct[0])));
    }

    @Test
    void authControllerGetMeNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authControllerGetMeLoggedIn() throws Exception {
        mockMvc.perform(get("/api/auth/me").with(oidcLogin()
                        .userInfoToken(token -> token
                                .claim("id", "1")
                                .claim("username", "testUser")
                                .claim("role", "USER"))
                        .idToken(token -> token
                                .claim("sub", "1"))).with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void authControllerGetMeLoggedIn2() throws Exception {
        mockMvc.perform(get("/api/auth/me").with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void login_shouldReturnLogin_WhenCalledWithUser() throws Exception {
        mockMvc.perform(post("/api/auth/login").with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testUser")));
    }

    @Test
    void register_shouldSaveUser_WhenCalledWithNewUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                                                {
                                                                  "username":"testUser2",
                                                                  "password":"testPassword"
                                                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    void register_shouldReturnException_WhenCalledWithOldUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                                                {
                                                                  "username":"testUser",
                                                                  "password":"testPassword"
                                                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void logout_shouldLogout_WhenCalled() throws Exception {
        mockMvc.perform(get("/api/auth/logout"))
                .andExpect(status().isOk());
    }

}