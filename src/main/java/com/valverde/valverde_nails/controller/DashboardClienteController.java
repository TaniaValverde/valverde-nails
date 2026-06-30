package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Cliente;
import com.valverde.valverde_nails.repository.CitaRepository;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardClienteController {

    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final CitaRepository citaRepository;

    public DashboardClienteController(
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository,
            CitaRepository citaRepository) {

        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.citaRepository = citaRepository;
    }

    @GetMapping("/dashboard-cliente")
    public String dashboardCliente(Model model, HttpSession session) {
        // ── Protección de sesión ──────────────────────────────────────────
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente != null && cliente.getIdCliente() != null) {
            model.addAttribute("nombreCliente", cliente.getNombre());

            java.util.List<com.valverde.valverde_nails.model.Cita> citas = citaRepository
                    .findByCliente_IdCliente(cliente.getIdCliente());

            model.addAttribute("totalCitas", citas.size());
            model.addAttribute("ultimaCita", citas.isEmpty() ? null : citas.get(citas.size() - 1));
        } else {
            model.addAttribute("totalCitas", 0);
            model.addAttribute("ultimaCita", null);
        }
        return "dashboard-cliente";
    }

    @GetMapping("/mis-citas")
    public String misCitas(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente != null && cliente.getIdCliente() != null) {
            model.addAttribute("citas", citaRepository.findByCliente_IdCliente(cliente.getIdCliente()));
        } else {
            model.addAttribute("citas", java.util.Collections.emptyList());
        }
        return "mis-citas";
    }

    @GetMapping("/agendar-cita")
    public String agendarCita(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        model.addAttribute("servicios", servicioRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        return "agendar-cita";
    }
}