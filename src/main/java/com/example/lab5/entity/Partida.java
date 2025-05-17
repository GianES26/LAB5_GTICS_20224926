package com.example.lab5.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class Partida implements Serializable {


    private LocalDateTime fecha;
    private int puntaje;
    private int respuestasCorrectas;

    public Partida(LocalDateTime fecha, int puntaje, int respuestasCorrectas) {
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.respuestasCorrectas = respuestasCorrectas;
    }

    public boolean isGanada() {
        return puntaje >= 60;
    }
}