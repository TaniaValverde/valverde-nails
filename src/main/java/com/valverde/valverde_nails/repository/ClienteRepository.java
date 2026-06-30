package com.valverde.valverde_nails.repository;

import com.valverde.valverde_nails.model.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

        Optional<Cliente> findByCorreo(String correo);

        Optional<Cliente> findByIdUsuario(Integer idUsuario);

}