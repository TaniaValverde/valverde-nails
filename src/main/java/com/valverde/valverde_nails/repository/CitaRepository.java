package com.valverde.valverde_nails.repository;

import com.valverde.valverde_nails.model.Cita;
import com.valverde.valverde_nails.model.Cliente;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByFechaOrderByHoraAsc(LocalDate fecha);

    List<Cita> findByCliente(Cliente cliente);

    // Buscar las citas por el idCliente de la entidad Cliente
    List<Cita> findByCliente_IdCliente(Integer idCliente);

    // Verificar si un cliente tiene citas registradas
    boolean existsByCliente_IdCliente(Integer idCliente);

}