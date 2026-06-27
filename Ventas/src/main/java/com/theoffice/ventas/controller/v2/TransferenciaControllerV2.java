package com.theoffice.ventas.controller.v2;

import com.theoffice.ventas.DTO.TransferenciaDTO;
import com.theoffice.ventas.model.Transferencia;
import com.theoffice.ventas.service.TransferenciaService;
import com.theoffice.ventas.assemblers.TransferenciaModelAssembler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/transferencias")
@Slf4j
public class TransferenciaControllerV2 {

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private TransferenciaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TransferenciaDTO>> crear(@Valid @RequestBody Transferencia transferencia) {
        log.info("API REST V2 - Petición POST para registrar una nueva transferencia");
        TransferenciaDTO nuevaTransferencia = transferenciaService.crear(transferencia);
        
        return ResponseEntity
                .created(linkTo(methodOn(TransferenciaControllerV2.class).obtenerPorId(nuevaTransferencia.getIdTransferencia())).toUri())
                .body(assembler.toModel(nuevaTransferencia));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TransferenciaDTO>>> obtenerTodas() {
        log.info("API REST V2 - Petición GET para listar todas las transferencias activas");
        
        List<EntityModel<TransferenciaDTO>> transferencias = transferenciaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (transferencias.isEmpty()) {
            log.info("La consulta de transferencias no retornó resultados activos");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
                transferencias,
                linkTo(methodOn(TransferenciaControllerV2.class).obtenerTodas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TransferenciaDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST V2 - Petición GET para buscar transferencia con ID: {}", id);
        TransferenciaDTO transferencia = transferenciaService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(transferencia));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TransferenciaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Transferencia transferencia) {
        log.info("API REST V2 - Petición PUT para actualizar transferencia con ID: {}", id);
        TransferenciaDTO actualizada = transferenciaService.actualizar(id, transferencia);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - Petición DELETE para baja lógica de la transferencia con ID: {}", id);
        transferenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}