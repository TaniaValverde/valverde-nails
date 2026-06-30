# 💅 Valverde Nails — Sistema de Gestión para Salón de Belleza

> Proyecto Final — Desarrollo de Software III  
> Universidad de Costa Rica, Sede del Sur

---

## 👥 Integrantes

| Nombre | Carné |
|--------|-------|
| Tania Valverde | C5I324 |


---

## 📋 Descripción

**Valverde Nails** es una aplicación web para la gestión de un salón de uñas. Permite administrar clientes, servicios, citas y pagos desde un panel de administración, y ofrece a los clientes un portal propio para agendar y consultar sus citas.

---

## 🛠️ Tecnologías utilizadas

| Capa | Tecnología |
|------|-----------|
| Backend | Java 21, Spring Boot 3.5 |
| Capa de presentación | Thymeleaf (equivalente a JSP, autorizado por el docente) |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | MySQL 8 |
| Frontend | Tailwind CSS, Material Symbols, Google Fonts |
| Servidor | Spring Boot Embedded Tomcat |
| Control de versiones | Git + GitHub |

---

##  Arquitectura

El sistema sigue el patrón **MVC (Model-View-Controller)**:

```
src/main/java/com/valverde/valverde_nails/
├── controller/   → Controladores Spring MVC (equivalente a Servlets)
├── model/        → Entidades JPA (Cliente, Usuario, Cita, Servicio, Pago)
├── repository/   → Interfaces Spring Data JPA (equivalente a DAO/JDBC)
src/main/resources/
├── templates/    → Vistas Thymeleaf (equivalente a JSP)
├── static/       → Recursos estáticos (CSS, imágenes)
└── application.properties → Configuración de BD y servidor
```

- **Model:** Entidades JPA mapeadas a tablas MySQL
- **View:** Templates Thymeleaf con Tailwind CSS
- **Controller:** Clases anotadas con `@Controller` que manejan rutas, sesiones y lógica de negocio
- **Repository:** Interfaces `JpaRepository` para acceso a datos con consultas parametrizadas (equivalente a JDBC con PreparedStatement)

---

##  Requisitos previos

- Java 21 o superior
- Maven 3.8+
- MySQL 8.0+
- IDE recomendado: IntelliJ IDEA o VS Code con extensión Java

---

##  Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/valverde-nails.git
cd valverde-nails
```

### 2. Crear y poblar la base de datos

Abrí MySQL Workbench o la terminal de MySQL y ejecutá el script completo:

```bash
mysql -u root -p < database/BASE_VALVERDE_STORE.sql
```

O copiá y pegá el contenido de `database/BASE_VALVERDE_STORE.sql` directamente en MySQL Workbench y ejecutalo.

Esto crea la base de datos `valverde_nails` con todas las tablas y datos de prueba incluidos.

### 3. Configurar la conexión a la base de datos

Editá el archivo `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/valverde_nails
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=none
```

### 4. Ejecutar el proyecto

```bash
mvn spring-boot:run
```

O desde tu IDE, ejecutá la clase `ValverdeNailsApplication.java`.

### 5. Abrir en el navegador

```
http://localhost:8080
```

---

##  Credenciales de prueba

### Administrador
| Campo | Valor |
|-------|-------|
| Correo | `admin@valverde.com` |
| Contraseña | `1234` |

### Clientes de prueba
| Correo | Contraseña |
|--------|-----------|
| `rocio@gmail.com` | `1234` |
| `valentina@gmail.com` | `Hola123` |
| `mariana@gmail.com` | `mariana123` |

---

##  Base de datos

El script SQL se encuentra en la carpeta `database/`:

```
database/
└── BASE_VALVERDE_STORE.sql   ← Crea la BD, tablas y datos de prueba
```

### Tablas del sistema

| Tabla | Descripción |
|-------|-------------|
| `roles` | Tipos de usuario (ADMINISTRADOR, CLIENTE) |
| `usuarios` | Credenciales de acceso al sistema |
| `clientes` | Información personal de los clientes |
| `servicios` | Catálogo de servicios del salón |
| `citas` | Citas agendadas (FK a clientes y servicios) |
| `pagos` | Historial de pagos registrados |

---

##  Módulos del sistema

### Panel Administrador
- **Dashboard** — Resumen estadístico (total clientes, citas, servicios, pagos)
- **Clientes** — CRUD completo con validación de correo duplicado
- **Servicios** — CRUD completo con validaciones del servidor
- **Citas** — CRUD completo con filtro de búsqueda en tiempo real
- **Pagos** — CRUD completo con validación de monto y método de pago

### Portal Cliente
- **Dashboard** — Resumen de citas del cliente
- **Mis citas** — Listado de citas agendadas
- **Agendar cita** — Formulario para crear nueva cita
- **Registro** — Auto-registro de nuevos clientes

---

## Seguridad implementada

- Sesiones con `HttpSession` — todas las rutas privadas verifican sesión activa
- Roles diferenciados — admin y cliente con acceso a rutas distintas
- Validaciones del lado del servidor en todos los módulos
- Consultas parametrizadas mediante JPA (protección contra SQL injection)
- Redirección a `/login` para usuarios no autenticados

---

## Estructura del repositorio

```
valverde-nails/
├── database/
│   └── BASE_VALVERDE_STORE.sql
├── src/
│   └── main/
│       ├── java/com/valverde/valverde_nails/
│       │   ├── controller/
│       │   ├── model/
│       │   └── repository/
│       └── resources/
│           ├── templates/
│           ├── static/
│           └── application.properties
├── pom.xml
└── README.md
```

---

##  Documentación

La documentación técnica completa del proyecto se encuentra en el archivo `Documentacion_Proyecto_Final.docx` e incluye análisis, diseño, diagramas ER, diccionario de datos, capturas de pantalla y pruebas realizadas.

---

*© 2026 Valverde Nails — Desarrollo de Software III, Universidad de Costa Rica*
