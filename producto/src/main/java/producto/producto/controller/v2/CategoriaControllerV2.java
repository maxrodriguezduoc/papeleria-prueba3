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
import producto.producto.assemblers.CategoriaModelAssembler;
import producto.producto.dto.CategoriaDTO;
import producto.producto.model.Categoria;
import producto.producto.service.CategoriaService;

@RestController
@RequestMapping("/api/v2/categorias")
@Slf4j
public class CategoriaControllerV2 {
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaDTO>> crear(@Valid @RequestBody Categoria categoria) {
        log.info("API REST - Petición POST para crear una nueva categoría: '{}'", categoria.getNombre());
        
        CategoriaDTO nuevaCategoria = categoriaService.crear(categoria);
        
        // Retorna un HTTP 201 Created con la URI en las cabeceras y el cuerpo con HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(CategoriaControllerV2.class).obtenerPorId(nuevaCategoria.getIdCategoria())).toUri())
                .body(assembler.toModel(nuevaCategoria));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> obtenerTodas() {
        log.info("API REST - Petición GET para listar todas las categorías activas");
        
        List<EntityModel<CategoriaDTO>> categorias = categoriaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (categorias.isEmpty()) {
            log.info("La consulta de categorías no retornó registros activos");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            categorias, linkTo(methodOn(CategoriaControllerV2.class).obtenerTodas()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar la categoría con ID: {}", id);
        
        CategoriaDTO categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Categoria categoria) {
        log.info("API REST - Petición PUT para actualizar la categoría con ID: {}", id);
        
        CategoriaDTO actualizada = categoriaService.actualizar(id, categoria);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica de la categoría con ID: {}", id);
        
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }
}