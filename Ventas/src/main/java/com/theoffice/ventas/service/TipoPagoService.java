package com.theoffice.ventas.service;

import com.theoffice.ventas.DTO.TipoPagoDTO;
import com.theoffice.ventas.model.TipoPago;
import com.theoffice.ventas.repository.TipoPagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TipoPagoService {

    @Autowired
    private TipoPagoRepository tipoPagoRepository;

    public TipoPagoDTO crear(TipoPago tipoPago) {
        log.info("Intentando registrar un nuevo tipo de pago: {}", tipoPago.getFormaPago());

        if (tipoPago.getFormaPago() == null || tipoPago.getFormaPago().trim().isEmpty()) {
            log.error("Falla al crear: El nombre del tipo de pago está vacío");
            throw new RuntimeException("El nombre del tipo de pago es obligatorio.");
        }

        tipoPago.setFormaPago(tipoPago.getFormaPago().trim());
        tipoPago.setActivo(true);

        TipoPago guardado = tipoPagoRepository.save(tipoPago);
        log.info("Tipo de pago registrado con éxito. ID asignado: {}", guardado.getIdTipoPago());

        return convertirADTO(guardado);
    }

    public List<TipoPagoDTO> obtenerTodos() {
        log.info("Consultando el listado de tipos de pago activos");
        
        return tipoPagoRepository.findAll().stream()
                .filter(TipoPago::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public TipoPagoDTO obtenerPorId(Integer id) {
        log.info("Buscando tipo de pago con ID: {}", id);
        
        TipoPago tipoPago = tipoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el tipo de pago con ID: " + id));

        if (!tipoPago.isActivo()) {
            log.warn("El tipo de pago ID {} existe pero está marcado como inactivo", id);
            throw new RuntimeException("El tipo de pago solicitado ya no está disponible.");
        }

        return convertirADTO(tipoPago);
    }

    public TipoPagoDTO actualizar(Integer id, TipoPago tipoPago) {
        log.info("Iniciando actualización para el tipo de pago ID: {}", id);
        
        TipoPago existente = tipoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Tipo de pago no encontrado."));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede modificar un tipo de pago que está inactivo.");
        }

        if (tipoPago.getFormaPago() == null || tipoPago.getFormaPago().trim().isEmpty()) {
            throw new RuntimeException("El nuevo nombre para el tipo de pago no puede estar vacío.");
        }

        existente.setFormaPago(tipoPago.getFormaPago().trim());
        log.info("Tipo de pago ID {} modificado con éxito", id);

        return convertirADTO(existente);
    }

    public void eliminar(Integer id) {
        log.warn("Procesando borrado lógico para el tipo de pago ID: {}", id);
        
        TipoPago tipoPago = tipoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: El ID ingresado no existe."));

        if (!tipoPago.isActivo()) {
            log.info("El tipo de pago ID {} ya se encontraba inactivo", id);
            return;
        }

        tipoPago.setActivo(false);
        tipoPagoRepository.save(tipoPago);
        
        log.info("El tipo de pago ID {} ha sido desactivado", id);
    }

    private TipoPagoDTO convertirADTO(TipoPago tipoPago) {
        TipoPagoDTO dto = new TipoPagoDTO();
        dto.setIdTipoPago(tipoPago.getIdTipoPago());
        dto.setFormaPago(tipoPago.getFormaPago());
        dto.setActivo(tipoPago.isActivo());
        return dto;
    }
}
