DROP DATABASE IF EXISTS valverde_nails;

CREATE DATABASE valverde_nails;

USE valverde_nails;

-- =====================================
-- TABLA ROLES
-- =====================================

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

INSERT INTO roles(nombre)
VALUES
('ADMINISTRADOR'),
('CLIENTE');

-- =====================================
-- TABLA USUARIOS
-- =====================================

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,

    CONSTRAINT fk_usuario_rol
    FOREIGN KEY (id_rol)
    REFERENCES roles(id_rol)
);

-- Administrador del sistema

INSERT INTO usuarios
(nombre, correo, password, id_rol)
VALUES
('Valverde Admin','admin@valverde.com','1234',1);

-- =====================================
-- TABLA CLIENTES
-- =====================================

CREATE TABLE clientes (

    id_cliente INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,

    telefono VARCHAR(20),

    correo VARCHAR(100),

    estado BOOLEAN DEFAULT TRUE,

    id_usuario INT UNIQUE,

    CONSTRAINT fk_cliente_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuarios(id_usuario)

);

INSERT INTO clientes
(nombre, telefono, correo, estado)
VALUES
('Maria Lopez','88881111','maria@gmail.com',TRUE),
('Juan Perez','88882222','juan@gmail.com',TRUE),
('Ana Mora','88883333','ana@gmail.com',TRUE);

-- =====================================
-- TABLA SERVICIOS
-- =====================================

CREATE TABLE servicios (

    id_servicio INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,

    descripcion VARCHAR(255),

    precio DECIMAL(10,2) NOT NULL,

    duracion INT NOT NULL

);

INSERT INTO servicios
(nombre, descripcion, precio, duracion)
VALUES
('Manicure Básico','Limpieza y esmalte',8000,60),
('Pedicure','Cuidado completo de pies',12000,90),
('Uñas Acrílicas','Aplicación de acrílico',18000,120);

-- =====================================
-- TABLA CITAS
-- =====================================

CREATE TABLE citas (

    id_cita INT AUTO_INCREMENT PRIMARY KEY,

    fecha DATE NOT NULL,

    hora TIME NOT NULL,

    estado VARCHAR(30) DEFAULT 'Pendiente',

    id_cliente INT NOT NULL,

    id_servicio INT NOT NULL,

    CONSTRAINT fk_cita_cliente
    FOREIGN KEY (id_cliente)
    REFERENCES clientes(id_cliente),

    CONSTRAINT fk_cita_servicio
    FOREIGN KEY (id_servicio)
    REFERENCES servicios(id_servicio)

);

INSERT INTO citas
(fecha,hora,estado,id_cliente,id_servicio)
VALUES
('2026-07-15','10:30:00','Pendiente',3,1);

-- =====================================
-- TABLA PAGOS
-- =====================================

CREATE TABLE pagos (

    id_pago INT AUTO_INCREMENT PRIMARY KEY,

    monto DECIMAL(10,2) NOT NULL,

    metodo_pago VARCHAR(50),

    nombre_cliente VARCHAR(100) NOT NULL,

    nombre_servicio VARCHAR(100) NOT NULL

);

-- Pago de ejemplo

INSERT INTO pagos
(monto, metodo_pago, nombre_cliente, nombre_servicio)
VALUES
(8000,'Efectivo','Ana Mora','Manicure Básico');

-- =====================================
-- CONSULTAS DE VERIFICACIÓN
-- =====================================

SELECT * FROM roles;
SELECT * FROM usuarios;
SELECT * FROM clientes;
SELECT * FROM servicios;
SELECT * FROM citas;
SELECT * FROM pagos;

SELECT * FROM clientes;
SELECT * FROM usuarios;


USE valverde_nails;
SELECT * FROM clientes;
SELECT * FROM usuarios;


USE valverde_nails;

-- Vincular Rocio y Maria que tienen correo coincidente
UPDATE clientes c
JOIN usuarios u ON c.correo = u.correo
SET c.id_usuario = u.id_usuario
WHERE c.id_usuario IS NULL;

-- Crear clientes para los usuarios que no tienen (Lucrecia, Nahomy, AnaLuis, Rosa)
INSERT INTO clientes (nombre, telefono, correo, estado, id_usuario)
SELECT u.nombre, NULL, u.correo, TRUE, u.id_usuario
FROM usuarios u
LEFT JOIN clientes c ON u.id_usuario = c.id_usuario
WHERE u.id_rol = 2 AND c.id_cliente IS NULL;

-- Verificar
SELECT c.id_cliente, c.nombre, c.correo, c.id_usuario
FROM clientes c ORDER BY c.id_cliente;



