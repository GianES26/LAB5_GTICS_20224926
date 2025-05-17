package com.example.lab5.controller;

import com.example.lab5.entity.Rol;
import com.example.lab5.entity.Usuario;
import com.example.lab5.repository.RolRepository;
import com.example.lab5.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/usuarios")
    public String redirectToLista() {
        return "redirect:/admin/usuarios/lista";
    }

    @GetMapping("/usuarios/lista")
    public String listUsers(Model model) {
        model.addAttribute("users", usuarioRepository.findAll());
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }

    @GetMapping("/usuarios/crear")
    public String showCreateForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("users", usuarioRepository.findAll());
        return "usuarios";
    }

    @PostMapping("/usuarios/crear")
    public String createUser(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", usuarioRepository.findAll());
            return "usuarios";
        }

        // Check for duplicate email or DNI
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            result.rejectValue("email", "error.usuario", "El correo ya está registrado.");
            model.addAttribute("users", usuarioRepository.findAll());
            return "usuarios";
        }
        if (usuarioRepository.findByDni(usuario.getDni()) != null) {
            result.rejectValue("dni", "error.usuario", "El DNI ya está registrado.");
            model.addAttribute("users", usuarioRepository.findAll());
            return "usuarios";
        }

        // Set default values
        usuario.setActivo(true);
        Rol userRol = rolRepository.findByNombre("USER");
        if (userRol == null) {
            result.rejectValue("rol", "error.usuario", "Rol USER no encontrado.");
            model.addAttribute("users", usuarioRepository.findAll());
            return "usuarios";
        }
        usuario.setRol(userRol);
        usuario.setPwd(passwordEncoder.encode(usuario.getPwd()));

        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente.");
        return "redirect:/admin/usuarios/lista";
    }
}