package com.example.lab5.controller;

import com.example.lab5.entity.Usuario;
import com.example.lab5.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {


    private final UsuarioRepository usuarioRepository;

    public HomeController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("home")
    public String home(Model model) {
        //List<Usuario> clinicList = usuarioRepository.findAll();

        //model.addAttribute("clinicList", clinicList);
        return "home";
    }

    @GetMapping("admin")
    public String adminHome(Model model) {
        //List<Usuario> clinicList = usuarioRepository.findAll();

        //model.addAttribute("clinicList", clinicList);
        return "adminHome";
    }

    @GetMapping("usuario")
    public String usuarioHome(Model model) {
        //List<Usuario> clinicList = usuarioRepository.findAll();

        //model.addAttribute("clinicList", clinicList);
        return "usuarioHome";
    }

}
