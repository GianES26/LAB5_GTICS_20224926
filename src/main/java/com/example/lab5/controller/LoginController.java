package com.example.lab5.controller;


import com.example.lab5.entity.Rol;
import com.example.lab5.entity.Usuario;

import com.example.lab5.repository.RolRepository;
import com.example.lab5.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    public LoginController(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    @GetMapping("/openLoginWindow")
    public String loginWindow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String rol = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .orElse("vecino");

            Map<String, String> redirectMap = new HashMap<>();
            redirectMap.put("ADMIN", "/admin");
            redirectMap.put("USUARIO", "/usuario");

            return "redirect:" + redirectMap.getOrDefault(rol, "/home");
        }
        return "loginWindow";
    }

}