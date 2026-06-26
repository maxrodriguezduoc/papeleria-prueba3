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
import producto.producto.assemblers.MarcaModelAssembler;
import producto.producto.dto.MarcaDTO;
import producto.producto.model.Marca;
import producto.producto.service.MarcaService;

@RestController
@RequestMapping("/api/v2/marcas")
@Slf4j
public class MarcaControllerV2 {
    @Autowired
    private MarcaService marcaService;

    @Autowired
    private MarcaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MarcaDTO>> crear(@Valid @RequestBody Marca marca) {
        log.info("API REST - POST crear marca: {}", marca.getNombre_marca());
        
        MarcaDTO nuevaMarca = marcaService.crearMarca(marca);
        
        // Retorna un HTTP 201 Created con la URI en las cabeceras y el cuerpo con HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(MarcaControllerV2.class).obtenerPorId(nuevaMarca.getId_marcas())).toUri())
                .body(assembler.toModel(nuevaMarca));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MarcaDTO>>> obtenerTodas() {
        log.info("API REST - GET listar marcas activas");
        
        List<EntityModel<MarcaDTO>> marcas = marcaService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (marcas.isEmpty()) {
            log.info("No se encontraron marcas activas");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            marcas, linkTo(methodOn(MarcaControllerV2.class).obtenerTodas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MarcaDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - GET buscar marca ID: {}", id);
        
        MarcaDTO marca = marcaService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(marca));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MarcaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Marca marca) {
        log.info("API REST - PUT actualizar marca ID: {}", id);
        
        MarcaDTO actualizada = marcaService.actualizarMarca(id, marca);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST - DELETE eliminar marca ID: {}", id);
        
        marcaService.eliminarMarca(id);
        return ResponseEntity.noContent().build(); 
    }
}
