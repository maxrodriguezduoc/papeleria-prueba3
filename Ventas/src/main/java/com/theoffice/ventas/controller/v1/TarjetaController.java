package com.theoffice.ventas.controller.v1;

import com.theoffice.ventas.DTO.TarjetaDTO;
import com.theoffice.ventas.model.Tarjeta;
import com.theoffice.ventas.service.TarjetaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarjetas")
@Slf4j
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @PostMapping
    public ResponseEntity<TarjetaDTO> crear(@Valid @RequestBody Tarjeta tarjeta) {
        log.info("API REST - Petición POST para registrar tarjeta");
        TarjetaDTO nuevaTarjeta = tarjetaService.crear(tarjeta);
        return new ResponseEntity<>(nuevaTarjeta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TarjetaDTO>> obtenerTodas() {
        log.info("API REST - Petición GET para listar todas las tarjetas activas");
        List<TarjetaDTO> tarjetas = tarjetaService.obtenerTodas();
        
        if (tarjetas.isEmpty()) {
            log.info("La consulta de tarjetas no retornó resultados activos");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(tarjetas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarjetaDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar tarjeta con ID: {}", id);
        TarjetaDTO tarjeta = tarjetaService.obtenerPorId(id);
        return ResponseEntity.ok(tarjeta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarjetaDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Tarjeta tarjeta) {
        log.info("API REST - Petición PUT para actualizar tarjeta con ID: {}", id);
        TarjetaDTO actualizada = tarjetaService.actualizar(id, tarjeta);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica de la tarjeta con ID: {}", id);
        tarjetaService.eliminar(id);
        return ResponseEntity.ok("La tarjeta con ID " + id + " ha sido desactivada con éxito.");
    }
}