package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Cliente;
import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public RegistroController(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository) {

        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario,
            @RequestParam String telefono,
            org.springframework.ui.Model model) {

        // ── Validación: correo duplicado ──────────────────────────────────
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()
                || clienteRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("errorCorreo", "Ya existe una cuenta registrada con ese correo.");
            return "registro";
        }

        // Siempre será un cliente
        usuario.setIdRol(2);

        // Guardar usuario
        usuario = usuarioRepository.save(usuario);

        // Crear automáticamente el cliente
        Cliente cliente = new Cliente();

        cliente.setNombre(usuario.getNombre());
        cliente.setCorreo(usuario.getCorreo());
        cliente.setTelefono(telefono);
        cliente.setEstado(true);
        cliente.setIdUsuario(usuario.getIdUsuario());

        clienteRepository.save(cliente);

        return "redirect:/login";
    }

}