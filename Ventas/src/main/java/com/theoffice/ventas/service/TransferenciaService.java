package com.theoffice.ventas.service;

import com.theoffice.ventas.DTO.TransferenciaDTO;
import com.theoffice.ventas.model.Transferencia;
import com.theoffice.ventas.repository.TransferenciaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    public TransferenciaDTO crear(Transferencia transferencia) {
        log.info("Iniciando registro de una nueva transferencia por un monto de: ${}", transferencia.getMonto());

        // No se puede transferir a la misma cuenta del mismo banco
        if (transferencia.getNumeroCuentaOrigen().equals(transferencia.getNumeroCuentaDestino()) 
                && transferencia.getBancoOrigen().equalsIgnoreCase(transferencia.getBancoDestino())) {
            log.error("Falla al crear: La cuenta de origen y destino son idénticas en el mismo banco");
            throw new RuntimeException("No es posible realizar transferencias entre la misma cuenta bancaria.");
        }

        // Validar que el monto no sea nulo ni menor o igual a cero
        if (transferencia.getMonto() == null || transferencia.getMonto() <= 0) {
            log.error("Falla al crear: Intento de transferir un monto inválido");
            throw new RuntimeException("El monto de la transferencia debe ser mayor a cero.");
        }

        // Validación de límite de monto (Extra)
        if (!validarLimiteMonto(transferencia.getMonto())) {
            log.error("Falla al crear: El monto ${} supera el límite permitido de ${}", 
                    transferencia.getMonto(), 2000000);
            throw new RuntimeException("El monto supera el límite máximo permitido por transacción ($" + 2000000 + ").");
        }

        // Validacion de formato numero de cuenta
        if (!validarFormatoCuenta(transferencia.getNumeroCuentaOrigen()) || 
            !validarFormatoCuenta(transferencia.getNumeroCuentaDestino())) {
            log.error("Falla al crear: Los números de cuenta contienen caracteres no numéricos");
            throw new RuntimeException("Los números de cuenta deben contener únicamente dígitos.");
        }

        transferencia.setActivo(true);

        Transferencia guardada = transferenciaRepository.save(transferencia);
        log.info("Transferencia procesada con éxito. ID asignado: {}", guardada.getIdTransferencia());

        return convertirADTO(guardada);
    }

    public List<TransferenciaDTO> obtenerTodas() {
        log.info("Consultando el histórico de transferencias activas");
        
        return transferenciaRepository.findAll().stream()
                .filter(Transferencia::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public TransferenciaDTO obtenerPorId(Integer id) {
        log.info("Buscando registro de transferencia con ID: {}", id);
        
        Transferencia transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro de transferencia con ID: " + id));

        if (!transferencia.isActivo()) {
            log.warn("La transferencia ID {} existe pero está marcada como inactiva", id);
            throw new RuntimeException("El registro de transferencia solicitado ya no está disponible.");
        }

        return convertirADTO(transferencia);
    }

    public TransferenciaDTO actualizar(Integer id, Transferencia transferencia) {
        log.info("Iniciando actualización de la transferencia ID: {}", id);
        
        Transferencia existente = transferenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Registro no encontrado."));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede modificar una transferencia que está inactiva.");
        }

        existente.setBancoOrigen(transferencia.getBancoOrigen());
        existente.setNumeroCuentaOrigen(transferencia.getNumeroCuentaOrigen());
        existente.setBancoDestino(transferencia.getBancoDestino());
        existente.setNumeroCuentaDestino(transferencia.getNumeroCuentaDestino());
        existente.setMonto(transferencia.getMonto());

        log.info("Registro de transferencia ID {} actualizado con éxito", id);

        return convertirADTO(existente);
    }

    public void eliminar(Integer id) {
        log.warn("Procesando baja lógica de transferencia ID: {}", id);
        
        Transferencia transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede anular: El ID ingresado no existe."));

        if (!transferencia.isActivo()) {
            log.info("La transferencia ID {} ya se encontraba inactiva", id);
            return;
        }

        transferencia.setActivo(false);
        transferenciaRepository.save(transferencia);
        
        log.info("La transferencia ID {} ha sido desactivada correctamente", id);
    }

    private boolean validarFormatoCuenta(String numeroCuenta) {
        if (numeroCuenta == null) {
            return false;
        }
        // Verifica que tenga solo dígitos (0 al 9)
        return numeroCuenta.matches("^[0-9]+$");
    }

    private boolean validarLimiteMonto(Integer monto) {
        if (monto == null) {
            return false;
        }
        return monto <= 2000000;
    }

    private TransferenciaDTO convertirADTO(Transferencia transferencia) {
        TransferenciaDTO dto = new TransferenciaDTO();
        dto.setIdTransferencia(transferencia.getIdTransferencia());
        dto.setBancoOrigen(transferencia.getBancoOrigen());
        dto.setNumeroCuentaOrigen(transferencia.getNumeroCuentaOrigen());
        dto.setBancoDestino(transferencia.getBancoDestino());
        dto.setNumeroCuentaDestino(transferencia.getNumeroCuentaDestino());
        dto.setMonto(transferencia.getMonto());
        dto.setActivo(transferencia.isActivo());
        return dto;
    }
}