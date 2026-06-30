package com.valverde.valverde_nails.repository;

import com.valverde.valverde_nails.model.Pago;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Reporte: pagos dentro de un rango de fechas
    List<Pago> findByFechaPagoBetween(LocalDate desde, LocalDate hasta);

    // Reporte: pagos filtrados por método de pago
    List<Pago> findByMetodoPagoContainingIgnoreCase(String metodoPago);

    // Reporte: pagos filtrados por método de pago y rango de fechas
    List<Pago> findByMetodoPagoContainingIgnoreCaseAndFechaPagoBetween(
            String metodoPago,
            LocalDate desde,
            LocalDate hasta);
}