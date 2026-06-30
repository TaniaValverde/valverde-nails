package com.valverde.valverde_nails.repository;

import com.valverde.valverde_nails.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
                extends JpaRepository<Usuario, Integer> {

        Usuario findByCorreoAndPassword(
                        String correo,
                        String password);

        Optional<Usuario> findByCorreo(String correo);

}