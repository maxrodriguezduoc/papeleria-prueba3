package com.cliente.cliente.controller;


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

import com.cliente.cliente.dto.ClienteDTO;
import com.cliente.cliente.model.Cliente;
import com.cliente.cliente.service.ClienteService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/clientes")
@Slf4j
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody Cliente cliente) {
        log.info("API REST - Petición POST para crear un cliente con RUT: {}", cliente.getRut());
        
        ClienteDTO nuevoCliente = clienteService.crear(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodos() {
        log.info("API REST - Petición GET para listar todos los clientes activos");
        
        List<ClienteDTO> clientes = clienteService.obtenerTodos();
        
        if (clientes.isEmpty()) {
            log.info("La consulta de clientes no retornó resultados");
            return ResponseEntity.noContent().build(); // Retorna 204 No Content si la lista está vacía
        }
        
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar cliente con ID: {}", id);
        
        ClienteDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        log.info("API REST - Petición PUT para actualizar el cliente con ID: {}", id);
        
        ClienteDTO actualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica del cliente con ID: {}", id);
        
        clienteService.eliminar(id);
        return ResponseEntity.ok("El cliente con ID " + id + " ha sido desactivado con éxito de la papelería.");
    }
}
