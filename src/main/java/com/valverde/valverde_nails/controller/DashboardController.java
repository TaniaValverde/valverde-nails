package com.valverde.valverde_nails.controller;

import com.valverde.valverde_nails.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (usuario.getIdRol() == 1) {
            return "redirect:/dashboard-admin";
        }

        if (usuario.getIdRol() == 2) {
            return "redirect:/dashboard-cliente";
        }

        return "redirect:/login";
    }
}