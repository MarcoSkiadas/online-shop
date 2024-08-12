package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AppUserDTO;
import org.example.backend.model.AppUser;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.service.AppUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;

    @GetMapping("/me")
    public AppUser getMe(@AuthenticationPrincipal OAuth2User user) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().matches("^\\d+$")) {
            return appUserRepository.findById(user.getName())
                    .orElseThrow();
        }
        return appUserService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @PostMapping("/login")
    public String login() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/register")
    public void register(@RequestBody AppUserDTO newUser) {
        appUserService.registerNewUser(newUser);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
    }
}
