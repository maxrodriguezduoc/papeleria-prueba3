package com.theoffice.ventas.controller;

import com.theoffice.ventas.DTO.PagoDTO;
import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.service.PagoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@Slf4j
public class PagoController {

    @Autowired
    private PagoService tipoPagoService;

    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody Pago tipoPago) {
        log.info("API REST - Creando tipo de pago: '{}'", tipoPago.getFormaPago());
        PagoDTO nuevoTipo = tipoPagoService.crear(tipoPago);
        return new ResponseEntity<>(nuevoTipo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> obtenerTodos() {
        log.info("API REST - Listando todos los tipos de pago activos");
        List<PagoDTO> tipos = tipoPagoService.obtenerTodos();
        
        if (tipos.isEmpty()) {
            log.info("No se encontraron tipos de pago activos");
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Buscando tipo de pago con ID: {}", id);
        PagoDTO tipoPago = tipoPagoService.obtenerPorId(id);
        return ResponseEntity.ok(tipoPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Pago tipoPago) {
        log.info("API REST - Actualizando tipo de pago con ID: {}", id);
        PagoDTO actualizado = tipoPagoService.actualizar(id, tipoPago);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Desactivando tipo de pago con ID: {}", id);
        tipoPagoService.eliminar(id);
        return ResponseEntity.ok("El método de pago ha sido desactivado con éxito.");
    }
}