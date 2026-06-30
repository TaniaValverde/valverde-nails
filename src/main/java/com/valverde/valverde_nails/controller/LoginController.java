package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Cliente;
import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public LoginController(UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String validarLogin(
            @RequestParam String correo,
            @RequestParam String password,
            HttpSession session) {

        Usuario usuario = usuarioRepository.findByCorreoAndPassword(
                correo,
                password);

        if (usuario != null) {

            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado", usuario);

            // Administrador
            if (usuario.getIdRol() == 1) {
                return "redirect:/dashboard-admin";
            }

            // Cliente
            if (usuario.getIdRol() == 2) {

                Cliente cliente = clienteRepository
                        .findByIdUsuario(usuario.getIdUsuario())
                        .orElse(null);

                session.setAttribute("clienteLogueado", cliente);

                return "redirect:/dashboard-cliente";
            }
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}