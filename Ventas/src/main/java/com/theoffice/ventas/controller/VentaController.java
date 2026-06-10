package com.theoffice.ventas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.theoffice.ventas.DTO.VentaDTO;
import com.theoffice.ventas.model.Venta;
import com.theoffice.ventas.service.VentaService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/ventas")
@Slf4j
public class VentaController {
    @Autowired
    private VentaService ventaService;

    @PostMapping("/producto/{idProducto}")
    public ResponseEntity<VentaDTO> crear(@PathVariable Integer idProducto,@Valid @RequestBody Venta venta) {
        log.info("API REST - POST crear venta para producto ID: {}", idProducto);
        VentaDTO nuevaVenta = ventaService.crear(venta, idProducto);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<VentaDTO>> obtenerTodas() {
        log.info("GET - Listar ventas activas");
        List<VentaDTO> ventas = ventaService.obtenerTodos();
        if (ventas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("GET - Buscar venta ID: {}", id);
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Venta venta) {
        log.info("PUT - Actualizar venta ID: {}", id);
        return ResponseEntity.ok(ventaService.actualizarVentas(id, venta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("DELETE - Eliminar venta ID: {}", id);
        ventaService.eliminar(id);
        return ResponseEntity.ok("Venta con ID " + id + " desactivada con éxito.");
    }

    

}
