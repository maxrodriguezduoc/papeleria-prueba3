package com.theoffice.ventas.controller.v2;

import com.theoffice.ventas.DTO.TarjetaDTO;
import com.theoffice.ventas.assemblers.TarjetaModelAssembler;
import com.theoffice.ventas.model.Tarjeta;
import com.theoffice.ventas.service.TarjetaService;
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
@RequestMapping("/api/v2/tarjetas")
@Slf4j
public class TarjetaControllerV2 {

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private TarjetaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TarjetaDTO>> crear(@Valid @RequestBody Tarjeta tarjeta) {
        log.info("API REST V2 - Petición POST para registrar tarjeta");
        TarjetaDTO nuevaTarjeta = tarjetaService.crear(tarjeta);
        
        return ResponseEntity
                .created(linkTo(methodOn(TarjetaControllerV2.class).obtenerPorId(nuevaTarjeta.getIdTarjeta())).toUri())
                .body(assembler.toModel(nuevaTarjeta));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TarjetaDTO>>> obtenerTodas() {
        log.info("API REST V2 - Petición GET para listar todas las tarjetas activas");
        
        List<EntityModel<TarjetaDTO>> tarjetas = tarjetaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (tarjetas.isEmpty()) {
            log.info("La consulta de tarjetas no retornó resultados activos");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
                tarjetas,
                linkTo(methodOn(TarjetaControllerV2.class).obtenerTodas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TarjetaDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST V2 - Petición GET para buscar tarjeta con ID: {}", id);
        TarjetaDTO tarjeta = tarjetaService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(tarjeta));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TarjetaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Tarjeta tarjeta) {
        log.info("API REST V2 - Petición PUT para actualizar tarjeta con ID: {}", id);
        TarjetaDTO actualizada = tarjetaService.actualizar(id, tarjeta);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - Petición DELETE para baja lógica de la tarjeta con ID: {}", id);
        tarjetaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}