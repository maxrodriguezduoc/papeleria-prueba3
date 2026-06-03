package com.theoffice.ventas.controller;

import com.theoffice.ventas.DTO.TipoPagoDTO;
import com.theoffice.ventas.model.TipoPago;
import com.theoffice.ventas.service.TipoPagoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-pago")
@Slf4j
public class TipoPagoController {

    @Autowired
    private TipoPagoService tipoPagoService;

    @PostMapping
    public ResponseEntity<TipoPagoDTO> crear(@Valid @RequestBody TipoPago tipoPago) {
        log.info("API REST - Creando tipo de pago: '{}'", tipoPago.getFormaPago());
        TipoPagoDTO nuevoTipo = tipoPagoService.crear(tipoPago);
        return new ResponseEntity<>(nuevoTipo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TipoPagoDTO>> obtenerTodos() {
        log.info("API REST - Listando todos los tipos de pago activos");
        List<TipoPagoDTO> tipos = tipoPagoService.obtenerTodos();
        
        if (tipos.isEmpty()) {
            log.info("No se encontraron tipos de pago activos");
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPagoDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Buscando tipo de pago con ID: {}", id);
        TipoPagoDTO tipoPago = tipoPagoService.obtenerPorId(id);
        return ResponseEntity.ok(tipoPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPagoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody TipoPago tipoPago) {
        log.info("API REST - Actualizando tipo de pago con ID: {}", id);
        TipoPagoDTO actualizado = tipoPagoService.actualizar(id, tipoPago);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Desactivando tipo de pago con ID: {}", id);
        tipoPagoService.eliminar(id);
        return ResponseEntity.ok("El método de pago ha sido desactivado con éxito.");
    }
}