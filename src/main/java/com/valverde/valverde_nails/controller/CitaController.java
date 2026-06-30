package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Cita;
import com.valverde.valverde_nails.model.Cliente;
import com.valverde.valverde_nails.model.Servicio;
import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.CitaRepository;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;

import java.beans.PropertyEditorSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    public CitaController(
            CitaRepository citaRepository,
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository) {

        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
    }

    // ── Verificaciones de sesión ────────────────────────────────────────────
    private boolean noEsAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        return u == null || u.getIdRol() != 1;
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
    }

    // ── Conversión de IDs del formulario a objetos ──────────────────────────
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Cliente.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.isEmpty()) {
                    setValue(clienteRepository.findById(Integer.valueOf(text)).orElse(null));
                }
            }
        });
        binder.registerCustomEditor(Servicio.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.isEmpty()) {
                    setValue(servicioRepository.findById(Integer.valueOf(text)).orElse(null));
                }
            }
        });
    }

    // ==========================================
    // RUTAS DEL ADMINISTRADOR
    // ==========================================
    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        model.addAttribute("citas", citaRepository.findAllByOrderByFechaAscHoraAsc());
        return "citas";
    }

    @GetMapping("/nuevo")
    public String nuevoAdmin(Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        model.addAttribute("cita", new Cita());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("servicios", servicioRepository.findAll());
        return "cita-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        Cita cita = citaRepository.findById(id).orElseThrow();
        model.addAttribute("cita", cita);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("servicios", servicioRepository.findAll());
        return "cita-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        citaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("exito", "Cita eliminada correctamente.");
        return "redirect:/citas";
    }

    // ==========================================
    // GUARDADO COMPARTIDO (admin y cliente)
    // ==========================================
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Cita cita,
            @RequestParam(value = "origen", defaultValue = "admin") String origen,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (noHaySesion(session))
            return "redirect:/login";

        // Si viene del cliente, asignar automáticamente el cliente en sesión
        if ("cliente".equals(origen)) {
            Cliente clienteSesion = (Cliente) session.getAttribute("clienteLogueado");

            if (clienteSesion != null && clienteSesion.getIdCliente() != null) {
                Cliente clienteDB = clienteRepository
                        .findById(clienteSesion.getIdCliente()).orElse(null);
                if (clienteDB != null) {
                    cita.setCliente(clienteDB);
                }
            }

            if (cita.getCliente() == null) {
                return "redirect:/login";
            }
        }

        // ── Validaciones del servidor ───────────────────────────────────────
        if (cita.getFecha() == null) {
            model.addAttribute("cita", cita);
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("servicios", servicioRepository.findAll());
            model.addAttribute("error", "Debe seleccionar una fecha para la cita.");
            return "cita-form";
        }

        if (cita.getHora() == null) {
            model.addAttribute("cita", cita);
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("servicios", servicioRepository.findAll());
            model.addAttribute("error", "Debe seleccionar una hora para la cita.");
            return "cita-form";
        }

        if (cita.getServicio() == null) {
            model.addAttribute("cita", cita);
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("servicios", servicioRepository.findAll());
            model.addAttribute("error", "Debe seleccionar un servicio.");
            return "cita-form";
        }

        citaRepository.save(cita);

        if ("cliente".equals(origen)) {
            redirectAttributes.addFlashAttribute("exito", "Cita agendada correctamente.");
            return "redirect:/agendar-cita?exito=true";
        }

        redirectAttributes.addFlashAttribute("exito", "Cita guardada correctamente.");
        return "redirect:/citas";
    }

    // ==========================================
    // RUTA DEL CLIENTE — nueva cita
    // ==========================================
    @GetMapping("/nueva")
    public String nuevoCliente(Model model, HttpSession session) {
        if (noHaySesion(session))
            return "redirect:/login";

        model.addAttribute("cita", new Cita());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("servicios", servicioRepository.findAll());
        return "dashboard-cliente";
    }
}