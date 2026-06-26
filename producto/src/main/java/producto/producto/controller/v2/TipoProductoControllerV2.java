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
import producto.producto.assemblers.TipoProductoModelAssembler;
import producto.producto.dto.TipoProductoDTO;
import producto.producto.model.TipoProducto;
import producto.producto.service.TipoProductoService;

@RestController
@RequestMapping("/api/v2/tipoProductos")
@Slf4j
public class TipoProductoControllerV2 {
    @Autowired
    private TipoProductoService tipoProductoService;

    @Autowired
    private TipoProductoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoProductoDTO>> crear(@Valid @RequestBody TipoProducto tipoProducto) {
        log.info("POST - Crear tipo de producto: {}", tipoProducto.getNombre());
        
        TipoProductoDTO nuevo = tipoProductoService.crear(tipoProducto);
        
        // Retorna un HTTP 201 Created con la URI en las cabeceras y el cuerpo con HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(TipoProductoControllerV2.class).obtenerPorId(nuevo.getId_tipo_producto())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<TipoProductoDTO>>> obtenerTodos() {
        log.info("GET - Listar tipos de producto activos");
        
        List<EntityModel<TipoProductoDTO>> tipos = tipoProductoService.obtenerActivos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (tipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            tipos, linkTo(methodOn(TipoProductoControllerV2.class).obtenerTodos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoProductoDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("GET - Buscar tipo de producto ID: {}", id);
        
        TipoProductoDTO tipoProducto = tipoProductoService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(tipoProducto));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<TipoProductoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody TipoProducto tipoProducto) {
        log.info("PUT - Actualizar tipo de producto ID: {}", id);
        
        TipoProductoDTO actualizado = tipoProductoService.actualizar(id, tipoProducto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("DELETE - Eliminar tipo de producto ID: {}", id);
        
        tipoProductoService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }
}
