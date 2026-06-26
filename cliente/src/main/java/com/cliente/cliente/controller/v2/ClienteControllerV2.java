package com.cliente.cliente.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cliente.cliente.assemblers.ClienteModelAssembler;
import com.cliente.cliente.dto.ClienteDTO;
import com.cliente.cliente.model.Cliente;
import com.cliente.cliente.service.ClienteService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v2/clientes")
@Slf4j
public class ClienteControllerV2 {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> crear(@Valid @RequestBody Cliente cliente) {
        log.info("API REST - Petición POST para crear un cliente con RUT: {}", cliente.getRut());
        
        ClienteDTO nuevoCliente = clienteService.crear(cliente);
        
        // Retorna un HTTP 201 Created con la URI en las cabeceras y el cuerpo con HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(nuevoCliente.getIdCliente())).toUri())
                .body(assembler.toModel(nuevoCliente));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> obtenerTodos() {
        log.info("API REST - Petición GET para listar todos los clientes activos");
        
        List<EntityModel<ClienteDTO>> clientes = clienteService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (clientes.isEmpty()) {
            log.info("La consulta de clientes no retornó resultados");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            clientes, linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar cliente con ID: {}", id);
        
        ClienteDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(cliente));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        log.info("API REST - Petición PUT para actualizar el cliente con ID: {}", id);
        
        ClienteDTO actualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica del cliente con ID: {}", id);
        
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }
}
