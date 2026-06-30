package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.CitaRepository;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.PagoRepository;
import com.valverde.valverde_nails.repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardAdminController {

    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;

    public DashboardAdminController(
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository,
            CitaRepository citaRepository,
            PagoRepository pagoRepository) {

        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.citaRepository = citaRepository;
        this.pagoRepository = pagoRepository;
    }

    @GetMapping("/dashboard-admin")
    public String dashboardAdmin(Model model, HttpSession session) {

        // ── Protección de sesión ────────────────────────────────────────────
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || usuario.getIdRol() != 1) {
            return "redirect:/login";
        }

        // Conteos globales para las tarjetas informativas
        model.addAttribute("totalClientes", clienteRepository.count());
        model.addAttribute("totalServicios", servicioRepository.count());
        model.addAttribute("totalCitas", citaRepository.count());
        model.addAttribute("totalPagos", pagoRepository.count());

        // Listado completo de citas para la tabla principal
        model.addAttribute("citas", citaRepository.findAll());

        // Listado completo de servicios para la sección derecha
        model.addAttribute("servicios", servicioRepository.findAll());

        return "dashboard-admin";
    }
}