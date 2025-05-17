package com.example.lab5.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Pregunta implements Serializable {

    private String texto;
    private List<String> opciones;
    private int respuestaCorrecta; // Índice de la opción correcta (0-3)

    public Pregunta(String texto, List<String> opciones, int respuestaCorrecta) {
        this.texto = texto;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }
}