package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AppUserDTO;
import org.example.backend.dto.AppUserResponse;
import org.example.backend.exceptions.InvalidIdException;
import org.example.backend.model.AppUser;
import org.example.backend.repository.AppUserRepository;
import org.example.backend.service.AppUserService;
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public AppUserResponse getMe(@AuthenticationPrincipal OAuth2User user) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().matches("^\\d+$")) {
            AppUser appUser = appUserRepository.findById(user.getName())
                    .orElseThrow();
            return AppUserResponse.fromAppUser(appUser);

        }
        return appUserService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public String login() {
        return appUserService.login();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@RequestBody AppUserDTO newUser) throws InvalidIdException {
        appUserService.registerNewUser(newUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
    }
}
