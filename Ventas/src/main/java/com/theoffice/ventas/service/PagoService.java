package com.theoffice.ventas.service;

import com.theoffice.ventas.DTO.PagoDTO;
import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public PagoDTO crear(Pago pago) {
        log.info("Iniciando registro de una nueva transacción de pago por un monto de: ${}", pago.getMonto());

        // 1. Validaciones para la propiedad String de formaPago
        if (pago.getFormaPago() == null || pago.getFormaPago().trim().isEmpty()) {
            log.error("Falla al registrar pago: La forma de pago está vacía");
            throw new RuntimeException("La forma de pago (Efectivo, Tarjeta, etc.) es estrictamente obligatoria.");
        }

        // 2. Validación del negocio transaccional (Monto positivo)
        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            log.error("Falla al registrar pago: Monto inválido (${})", pago.getMonto());
            throw new RuntimeException("El monto a pagar debe ser una cantidad mayor a cero.");
        }

        // Normalizamos el texto ingresado a mayúsculas para mantener consistencia en la BD
        pago.setFormaPago(pago.getFormaPago().trim().toUpperCase());
        pago.setActivo(true);

        Pago guardado = pagoRepository.save(pago);
        log.info("Pago registrado con éxito en el sistema. ID Transacción: {}", guardado.getIdPago());

        return convertirADTO(guardado);
    }

    public List<PagoDTO> obtenerTodos() {
        log.info("Consultando el historial completo de pagos activos");
        
        return pagoRepository.findAll().stream()
                .filter(Pago::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public PagoDTO obtenerPorId(Integer id) {
        log.info("Buscando registro de pago con ID: {}", id);
        
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún registro de pago con el ID: " + id));

        if (!pago.isActivo()) {
            log.warn("El pago con ID {} se encuentra anulado o inactivo", id);
            throw new RuntimeException("El registro de pago solicitado no está disponible o fue anulado.");
        }

        return convertirADTO(pago);
    }

    public PagoDTO actualizar(Integer id, Pago pago) {
        log.info("Iniciando modificación del registro de pago ID: {}", id);
        
        Pago existente = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Transacción de pago no encontrada."));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede modificar un pago que ha sido previamente anulado.");
        }

        // Validaciones del String en la actualización
        if (pago.getFormaPago() == null || pago.getFormaPago().trim().isEmpty()) {
            throw new RuntimeException("La nueva forma de pago no puede estar vacía.");
        }

        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            throw new RuntimeException("El nuevo monto debe ser una cantidad mayor a cero.");
        }

        existente.setFormaPago(pago.getFormaPago().trim().toUpperCase());
        existente.setMonto(pago.getMonto());
        
        // Si el pago viene con datos de tarjetas o transferencias mutables, se actualizan aquí
        if (pago.getTarjeta() != null) existente.setTarjeta(pago.getTarjeta());
        if (pago.getTransferencia() != null) existente.setTransferencia(pago.getTransferencia());

        log.info("Registro de pago ID {} actualizado correctamente", id);
        return convertirADTO(existente);
    }

    public void eliminar(Integer id) {
        log.warn("Ejecutando anulación lógica para el registro de pago ID: {}", id);
        
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede anular: El ID de pago no existe."));

        if (!pago.isActivo()) {
            log.info("El pago ID {} ya se encontraba marcado como inactivo/anulado", id);
            return;
        }

        pago.setActivo(false);
        pagoRepository.save(pago);
        
        log.info("El pago ID {} ha sido anulado con éxito", id);
    }

    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        
        // Ajustado para reflejar las propiedades reales de la transacción de pago
        dto.setIdPago(pago.getIdPago());
        dto.setFormaPago(pago.getFormaPago());
        dto.setMonto(pago.getMonto());
        dto.setActivo(pago.isActivo());
        
        // Evitamos bucles infinitos mapeando solo el ID de la venta si existe
        if (pago.getVenta() != null) {
            dto.setIdVenta(pago.getVenta().getId_venta());
        }
        
        return dto;
    }
}
