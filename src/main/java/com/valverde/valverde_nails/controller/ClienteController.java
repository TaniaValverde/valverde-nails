package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Cliente;
import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.CitaRepository;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClienteController {

    private final ClienteRepository clienteRepository;

    private final CitaRepository citaRepository;

    private final UsuarioRepository usuarioRepository;

    ClienteController(ClienteRepository clienteRepository, CitaRepository citaRepository,
            UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ── Método de verificación de sesión ──────────────────────────────────────
    private boolean noEsAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        return u == null || u.getIdRol() != 1;
    }

    // ── LISTAR CLIENTES ────────────────────────────────────────────────────────
    @GetMapping("/clientes")
    public String listarClientes(HttpSession session, Model model) {
        if (noEsAdmin(session))
            return "redirect:/login";
        model.addAttribute("clientes", clienteRepository.findAll());
        return "clientes";
    }

    // ── NUEVO CLIENTE ──────────────────────────────────────────────────────────
    @GetMapping("/clientes/nuevo")
    public String nuevoCliente(HttpSession session, Model model) {
        if (noEsAdmin(session))
            return "redirect:/login";
        model.addAttribute("cliente", new Cliente());
        return "cliente-form";
    }

    // ── GUARDAR CLIENTE ────────────────────────────────────────────────────────
    @PostMapping("/clientes/guardar")
    public String guardarCliente(
            HttpSession session,
            @ModelAttribute Cliente cliente,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        boolean esNuevo = (cliente.getIdCliente() == null);

        // ── Validación: correo duplicado (solo al crear, no al editar) ────────
        if (esNuevo) {
            boolean correoEnClientes = clienteRepository
                    .findByCorreo(cliente.getCorreo()).isPresent();
            boolean correoEnUsuarios = usuarioRepository
                    .findByCorreo(cliente.getCorreo()).isPresent();

            if (correoEnClientes || correoEnUsuarios) {
                model.addAttribute("cliente", cliente);
                model.addAttribute("errorCorreo",
                        "Ya existe una cuenta registrada con ese correo electrónico.");
                return "cliente-form";
            }
        }

        cliente.setEstado(true);

        // ── Crear usuario automáticamente solo para clientes nuevos ───────────
        if (esNuevo && cliente.getIdUsuario() == null) {

            System.out.println("Contraseña recibida: " + password);
            Usuario usuario = new Usuario();
            usuario.setNombre(cliente.getNombre());
            usuario.setCorreo(cliente.getCorreo());
            usuario.setPassword(password); // contraseña proporcionada
            usuario.setIdRol(2); // rol = cliente

            usuario = usuarioRepository.save(usuario);
            cliente.setIdUsuario(usuario.getIdUsuario());
        }

        clienteRepository.save(cliente);
        String msg = esNuevo ? "Cliente creado correctamente." : "Cliente actualizado correctamente.";
        redirectAttributes.addFlashAttribute("eliminado", msg);
        return "redirect:/clientes";
    }

    // ── EDITAR CLIENTE ─────────────────────────────────────────────────────────
    @GetMapping("/clientes/editar/{id}")
    public String editarCliente(HttpSession session, @PathVariable Integer id, Model model) {
        if (noEsAdmin(session))
            return "redirect:/login";
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);
        return "cliente-form";
    }

    // ── ELIMINAR CLIENTE ──────────────────────────────────────────────────────
    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(
            HttpSession session,
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        if (!citaRepository.findByCliente_IdCliente(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el cliente porque tiene citas registradas.");
            return "redirect:/clientes";
        }

        clienteRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("eliminado", "Cliente eliminado correctamente.");
        return "redirect:/clientes";
    }
}