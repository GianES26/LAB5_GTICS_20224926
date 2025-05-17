package com.example.lab5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int idusuario;
    @Column(name="nombre", nullable = false, length = 45)
    private String nombre;
    @Column(name="apellido", nullable = false, length = 45)
    private String apellido;
    @Column(name="dni", nullable = false, unique = true, length = 8)
    private String dni;
    @Column(name="email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name="edad", nullable = false)
    private Integer edad;
    @Column(name="pwd", nullable = false, length = 100)
    private String pwd;
    @Column(name="activo", nullable = false)
    private Boolean activo;
    @ManyToOne
    @JoinColumn(name = "idrol", nullable = false)
    private Rol rol;

}

/*package com.example.lab5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private int idrol;

    @Column(name = "nombre", nullable = false, unique = true, length = 45)
    private String nombre;
}*/
