package com.theoffice.ventas.controller.v2;

import com.theoffice.ventas.DTO.PagoDTO;
import com.theoffice.ventas.assemblers.PagoModelAssembler;
import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.service.PagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v2/pagos")
@Slf4j
public class PagoControllerV2 {

    @Autowired
    private final PagoService pagoService;

    @Autowired
    private final PagoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> crear(@Valid @RequestBody Pago tipoPago) {
        log.info("API REST V2 - Creando tipo de pago: '{}'", tipoPago.getFormaPago());
        PagoDTO nuevoTipo = pagoService.crear(tipoPago);
        
        return ResponseEntity
                .created(linkTo(methodOn(PagoControllerV2.class).obtenerPorId(nuevoTipo.getIdPago())).toUri())
                .body(assembler.toModel(nuevoTipo));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> obtenerTodos() {
        log.info("API REST V2 - Listando todos los tipos de pago activos");
        List<EntityModel<PagoDTO>> tipos = pagoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (tipos.isEmpty()) {
            log.info("No se encontraron tipos de pago activos");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
                tipos,
                linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST V2 - Buscando tipo de pago con ID: {}", id);
        PagoDTO tipoPago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(tipoPago));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Pago tipoPago) {
        log.info("API REST V2 - Actualizando tipo de pago con ID: {}", id);
        PagoDTO actualizado = pagoService.actualizar(id, tipoPago);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - Desactivando tipo de pago con ID: {}", id);
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}