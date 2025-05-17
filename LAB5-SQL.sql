# Nota: Pruebe la pagina para admin, hasta la pregunta 3 funciona como se debe, 
# la pregunta 4 la hice a medias :(, 
# si ingresa a la página con una cuenta de usuario la contraseña

# Nota: Al usuario admin se ingresa con el correo juan.perez@admin.com y la contraseña es 123, 
# como en la base de datos se almacenan string hasheados '123' se convierte en
# '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm'

# Nota: Las contraseñas para todos los usuarios ingresados desde la base de datos es 123 (ya sean
# admin o usuario), para los usuarios que cree desde las vistas de admin sus contraseñas
# serán las que ingrese en la vista de creacion, en la base de datos aparecerán hasheadas

CREATE SCHEMA LAB5;

USE LAB5;

CREATE TABLE rol (
    idrol INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL UNIQUE
);

CREATE TABLE usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    edad INT NOT NULL,
    pwd VARCHAR(100) NOT NULL,
    activo TINYINT NOT NULL DEFAULT 1,
    idrol INT NOT NULL,
    FOREIGN KEY (idrol) REFERENCES rol(idrol)
);


INSERT INTO rol (nombre) VALUES ('ADMIN'), ('USER');

# Cuando quiera loguearse el pwd de la cuenta es 123, se hashea de 
# '123' a  '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm'
INSERT INTO usuario (nombre, apellido, dni, email, edad, pwd, activo, idrol)
VALUES ('Juan', 'Pérez', '12345678', 'juan.perez@admin.com', 35, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1,
    (SELECT idrol FROM rol WHERE nombre = 'ADMIN'));


INSERT INTO usuario (nombre, apellido, dni, email, edad, pwd, activo, idrol) VALUES
('Ana', 'Ramírez', '87654321', 'ana.ramirez@correo.com', 28, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Carlos', 'Fernández', '12348765', 'carlos.fernandez@correo.com', 30, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Lucía', 'Torres', '23456789', 'lucia.torres@correo.com', 22, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Pedro', 'Sánchez', '34567890', 'pedro.sanchez@correo.com', 27, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('María', 'Gonzales', '45678901', 'maria.gonzales@correo.com', 33, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Jorge', 'Mendoza', '56789012', 'jorge.mendoza@correo.com', 24, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Sofía', 'Reyes', '67890123', 'sofia.reyes@correo.com', 29, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Diego', 'Castro', '78901234', 'diego.castro@correo.com', 31, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Elena', 'Ruiz', '89012345', 'elena.ruiz@correo.com', 26, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER')),
('Mateo', 'Vargas', '90123456', 'mateo.vargas@correo.com', 32, '$2a$04$gz2sb1sDjG4g8MPrTNcmo.ZK1L8rgL5iTytPiog3Y9s1w4gbnw6qm', 1, (SELECT idrol FROM rol WHERE nombre = 'USER'));
