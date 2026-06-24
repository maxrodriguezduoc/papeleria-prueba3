package com.theoffice.ventas.controller.v2;

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

import com.theoffice.ventas.DTO.VentaDTO;
import com.theoffice.ventas.model.Venta;
import com.theoffice.ventas.service.VentaService;
import com.theoffice.ventas.assemblers.VentaModelAssembler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/ventas")
@Slf4j
public class VentaControllerV2 {
    
    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaModelAssembler assembler;

    @PostMapping(value = "/producto/{idProducto}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VentaDTO>> crear(@PathVariable Integer idProducto, @Valid @RequestBody Venta venta) {
        log.info("API REST V2 - POST crear venta para producto ID: {}", idProducto);
        VentaDTO nuevaVenta = ventaService.crear(venta, idProducto);
        
        return ResponseEntity
                .created(linkTo(methodOn(VentaControllerV2.class).obtenerPorId(nuevaVenta.getId_venta())).toUri())
                .body(assembler.toModel(nuevaVenta));
    }
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<VentaDTO>>> obtenerTodas() {
        log.info("API REST V2 - GET Listar ventas activas");
        List<EntityModel<VentaDTO>> ventas = ventaService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (ventas.isEmpty()) return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(CollectionModel.of(
                ventas,
                linkTo(methodOn(VentaControllerV2.class).obtenerTodas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VentaDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST V2 - GET Buscar venta ID: {}", id);
        VentaDTO venta = ventaService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(venta));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<VentaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Venta venta) {
        log.info("API REST V2 - PUT Actualizar venta ID: {}", id);
        VentaDTO ventaActualizada = ventaService.actualizarVentas(id, venta);
        return ResponseEntity.ok(assembler.toModel(ventaActualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - DELETE Eliminar venta ID: {}", id);
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }
}