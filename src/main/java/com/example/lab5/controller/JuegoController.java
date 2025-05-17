package com.example.lab5.controller;

import com.example.lab5.entity.Pregunta;
import com.example.lab5.entity.Partida;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class JuegoController {

    private final List<Pregunta> preguntas;

    public JuegoController() {
        // Initialize 10 questions in memory
        preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("¿Cuál es la capital de Francia?", List.of("París", "Londres", "Berlín", "Madrid"), 0));
        preguntas.add(new Pregunta("¿En qué año llegó el hombre a la luna?", List.of("1969", "1972", "1965", "1980"), 0));
        preguntas.add(new Pregunta("¿Cuál es el planeta más grande del sistema solar?", List.of("Júpiter", "Saturno", "Tierra", "Marte"), 0));
        preguntas.add(new Pregunta("¿Quién pintó la Mona Lisa?", List.of("Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Claude Monet"), 0));
        preguntas.add(new Pregunta("¿Cuál es el río más largo del mundo?", List.of("Amazonas", "Nilo", "Yangtsé", "Misisipi"), 0));
        preguntas.add(new Pregunta("¿En qué continente está Egipto?", List.of("África", "Asia", "Europa", "Oceanía"), 0));
        preguntas.add(new Pregunta("¿Cuál es el elemento químico con símbolo O?", List.of("Oxígeno", "Oro", "Osmio", "Oganesón"), 0));
        preguntas.add(new Pregunta("¿Quién escribió 'Cien años de soledad'?", List.of("Gabriel García Márquez", "Mario Vargas Llosa", "Julio Cortázar", "Jorge Luis Borges"), 0));
        preguntas.add(new Pregunta("¿Cuál es la moneda de Japón?", List.of("Yen", "Won", "Yuan", "Dólar"), 0));
        preguntas.add(new Pregunta("¿Qué animal es conocido como el rey de la selva?", List.of("León", "Tigre", "Elefante", "Gorila"), 0));
    }

    @GetMapping("/juego")
    public String iniciarJuego(Model model, HttpSession session) {
        // Select 5 random questions
        List<Pregunta> preguntasSeleccionadas = new ArrayList<>(preguntas);
        Collections.shuffle(preguntasSeleccionadas);
        preguntasSeleccionadas = preguntasSeleccionadas.subList(0, 5);

        // Store questions in session
        session.setAttribute("preguntasJuego", preguntasSeleccionadas);
        session.setAttribute("preguntaActual", 0);
        session.setAttribute("puntaje", 0);
        session.setAttribute("respuestasCorrectas", 0);

        // Pass first question to template
        model.addAttribute("pregunta", preguntasSeleccionadas.get(0));
        model.addAttribute("indicePregunta", 0);
        model.addAttribute("totalPreguntas", 5);

        return "usuario/juego";
    }

    @PostMapping("/juego/responder")
    public String procesarRespuesta(
            @RequestParam("respuesta") List<String> respuestas,
            @RequestParam("indicePregunta") int indicePregunta,
            Model model, HttpSession session) {
        // Validate single answer
        if (respuestas.size() != 1) {
            model.addAttribute("error", "Por favor, selecciona exactamente una respuesta.");
            List<Pregunta> preguntasJuego = (List<Pregunta>) session.getAttribute("preguntasJuego");
            model.addAttribute("pregunta", preguntasJuego.get(indicePregunta));
            model.addAttribute("indicePregunta", indicePregunta);
            model.addAttribute("totalPreguntas", 5);
            return "usuario/juego";
        }

        // Get session data
        List<Pregunta> preguntasJuego = (List<Pregunta>) session.getAttribute("preguntasJuego");
        Integer puntaje = (Integer) session.getAttribute("puntaje");
        Integer respuestasCorrectas = (Integer) session.getAttribute("respuestasCorrectas");

        // Check answer
        Pregunta preguntaActual = preguntasJuego.get(indicePregunta);
        int respuestaUsuario = Integer.parseInt(respuestas.get(0));
        if (respuestaUsuario == preguntaActual.getRespuestaCorrecta()) {
            puntaje += 20;
            respuestasCorrectas++;
            session.setAttribute("puntaje", puntaje);
            session.setAttribute("respuestasCorrectas", respuestasCorrectas);
        }

        // Move to next question or show results
        int siguienteIndice = indicePregunta + 1;
        if (siguienteIndice < preguntasJuego.size()) {
            session.setAttribute("preguntaActual", siguienteIndice);
            model.addAttribute("pregunta", preguntasJuego.get(siguienteIndice));
            model.addAttribute("indicePregunta", siguienteIndice);
            model.addAttribute("totalPreguntas", 5);
            return "usuario/juego";
        } else {
            // End of game, save Partida
            Partida partida = new Partida(LocalDateTime.now(), puntaje, respuestasCorrectas);
            session.setAttribute("ultimaPartida", partida);
            return "redirect:/usuario/juego/resultado";
        }
    }

    @GetMapping("/juego/resultado")
    public String mostrarResultado(Model model, HttpSession session) {
        Partida partida = (Partida) session.getAttribute("ultimaPartida");
        if (partida == null) {
            return "redirect:/usuario/juego";
        }
        model.addAttribute("partida", partida);
        return "usuario/juego";
    }
}