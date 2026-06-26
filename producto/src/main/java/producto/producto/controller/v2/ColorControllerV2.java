package producto.producto.controller.v2;

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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import producto.producto.assemblers.ColorModelAssembler;
import producto.producto.dto.ColorDTO;
import producto.producto.model.Color;
import producto.producto.service.ColorService;

@RestController
@RequestMapping("/api/v2/colores")
@Slf4j
public class ColorControllerV2 {
    @Autowired
    private ColorService colorService;

    @Autowired
    private ColorModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ColorDTO>> crear(@Valid @RequestBody Color color) {
        log.info("POST - Crear color: {}", color.getNombre_color());
        
        ColorDTO nuevo = colorService.crear(color);
        
        // Retorna un HTTP 201 Created con la URI en las cabeceras y el cuerpo con HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(ColorControllerV2.class).obtenerPorId(nuevo.getId_color())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ColorDTO>>> obtenerTodos() {
        log.info("GET - Listar colores activos");
        
        List<EntityModel<ColorDTO>> colores = colorService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (colores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            colores, linkTo(methodOn(ColorControllerV2.class).obtenerTodos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ColorDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("GET - Buscar color ID: {}", id);
        
        ColorDTO color = colorService.buscarColorPorId(id);
        return ResponseEntity.ok(assembler.toModel(color));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ColorDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Color color) {
        log.info("PUT - Actualizar color ID: {}", id);
        
        ColorDTO actualizado = colorService.actualizarColor(id, color);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("DELETE - Eliminar color ID: {}", id);
        
        colorService.eliminarColor(id);
        return ResponseEntity.noContent().build(); 
    }
}