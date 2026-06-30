package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Pago;
import com.valverde.valverde_nails.model.Usuario;
import com.valverde.valverde_nails.repository.ClienteRepository;
import com.valverde.valverde_nails.repository.PagoRepository;
import com.valverde.valverde_nails.repository.ServicioRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoRepository pagoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    public PagoController(PagoRepository pagoRepository,
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository) {
        this.pagoRepository = pagoRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
    }

    // ── Verificación de sesión de administrador ─────────────────────────────
    private boolean noEsAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        return u == null || u.getIdRol() != 1;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        model.addAttribute("pagos", pagoRepository.findAll());
        return "pagos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        model.addAttribute("pago", new Pago());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("servicios", servicioRepository.findAll());
        return "pago-form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Pago pago,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        // ── Validaciones del servidor ───────────────────────────────────────
        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            model.addAttribute("pago", pago);
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("servicios", servicioRepository.findAll());
            model.addAttribute("error", "El monto debe ser mayor a cero.");
            return "pago-form";
        }

        if (pago.getNombreCliente() == null || pago.getNombreCliente().isBlank()) {
            model.addAttribute("pago", pago);
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("servicios", servicioRepository.findAll());
            model.addAttribute("error", "Debe seleccionar un cliente.");
            return "pago-form";
        }

        if (pago.getMetodoPago() == null || pago.getMetodoPago().isBlank()) {
            model.addAttribute("pago", pago);
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("servicios", servicioRepository.findAll());
            model.addAttribute("error", "Debe seleccionar un método de pago.");
            return "pago-form";
        }

        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        }

        pagoRepository.save(pago);
        redirectAttributes.addFlashAttribute("exito", "Pago registrado correctamente.");
        return "redirect:/pagos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {
        if (noEsAdmin(session))
            return "redirect:/login";

        Pago pago = pagoRepository.findById(id).orElseThrow();
        model.addAttribute("pago", pago);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("servicios", servicioRepository.findAll());
        return "pago-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (noEsAdmin(session))
            return "redirect:/login";

        pagoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("exito", "Pago eliminado correctamente.");
        return "redirect:/pagos";
    }
}