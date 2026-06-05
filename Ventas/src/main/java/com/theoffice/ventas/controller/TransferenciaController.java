package com.theoffice.ventas.controller;

import com.theoffice.ventas.DTO.TransferenciaDTO;
import com.theoffice.ventas.model.Transferencia;
import com.theoffice.ventas.service.TransferenciaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transferencias")
@Slf4j
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaDTO> crear(@Valid @RequestBody Transferencia transferencia) {
        log.info("API REST - Petición POST para registrar una nueva transferencia");
        TransferenciaDTO nuevaTransferencia = transferenciaService.crear(transferencia);
        return new ResponseEntity<>(nuevaTransferencia, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaDTO>> obtenerTodas() {
        log.info("API REST - Petición GET para listar todas las transferencias activas");
        List<TransferenciaDTO> transferencias = transferenciaService.obtenerTodas();
        
        if (transferencias.isEmpty()) {
            log.info("La consulta de transferencias no retornó resultados activos");
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar transferencia con ID: {}", id);
        TransferenciaDTO transferencia = transferenciaService.obtenerPorId(id);
        return ResponseEntity.ok(transferencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Transferencia transferencia) {
        log.info("API REST - Petición PUT para actualizar transferencia con ID: {}", id);
        TransferenciaDTO actualizada = transferenciaService.actualizar(id, transferencia);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica de la transferencia con ID: {}", id);
        transferenciaService.eliminar(id);
        return ResponseEntity.ok("La transferencia con ID " + id + " ha sido desactivada con éxito.");
    }
}