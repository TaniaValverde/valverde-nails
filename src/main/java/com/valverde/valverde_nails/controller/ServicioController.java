package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Servicio;
import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    // ── Verificación de sesión de administrador ─────────────────────────────
    private boolean noEsAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        return u == null || u.getIdRol() != 1;
    }

    @GetMapping("/servicios")
    public String listarServicios(Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        model.addAttribute("servicios", servicioRepository.findAll());
        return "servicios";
    }

    @GetMapping("/servicios/nuevo")
    public String nuevoServicio(Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        model.addAttribute("servicio", new Servicio());
        return "servicio-form";
    }

    @PostMapping("/servicios/guardar")
    public String guardarServicio(
            @ModelAttribute Servicio servicio,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        // ── Validaciones del servidor ───────────────────────────────────────
        if (servicio.getNombre() == null || servicio.getNombre().isBlank()) {
            model.addAttribute("servicio", servicio);
            model.addAttribute("error", "El nombre del servicio es obligatorio.");
            return "servicio-form";
        }

        if (servicio.getPrecio() == null || servicio.getPrecio() <= 0) {
            model.addAttribute("servicio", servicio);
            model.addAttribute("error", "El precio debe ser mayor a cero.");
            return "servicio-form";
        }

        if (servicio.getDuracion() == null || servicio.getDuracion() <= 0) {
            model.addAttribute("servicio", servicio);
            model.addAttribute("error", "La duración debe ser mayor a cero.");
            return "servicio-form";
        }

        servicioRepository.save(servicio);
        redirectAttributes.addFlashAttribute("exito", "Servicio guardado correctamente.");
        return "redirect:/servicios";
    }

    @GetMapping("/servicios/editar/{id}")
    public String editarServicio(
            @PathVariable Integer id,
            Model model,
            HttpSession session) {

        if (noEsAdmin(session))
            return "redirect:/login";

        Servicio servicio = servicioRepository.findById(id).orElse(null);
        model.addAttribute("servicio", servicio);
        return "servicio-form";
    }

    @GetMapping("/servicios/eliminar/{id}")
    public String eliminarServicio(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        servicioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("exito", "Servicio eliminado correctamente.");
        return "redirect:/servicios";
    }

    @GetMapping("/servicios-cliente")
    public String listarServiciosCliente(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        return "servicios-cliente";
    }
}