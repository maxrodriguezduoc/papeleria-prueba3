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
import producto.producto.assemblers.ProductoModelAssembler;
import producto.producto.controller.ProductoController;
import producto.producto.dto.ProductoDTO;
import producto.producto.model.Producto;
import producto.producto.service.ProductoService;

@RestController
@RequestMapping("/api/v2/productos")
@Slf4j
public class ProductoControllerV2 {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoDTO>> crear(@Valid @RequestBody Producto producto) {
        log.info("API REST - Petición POST para crear un producto con nombre: {}", producto.getNombre_producto());
        
        ProductoDTO nuevoProducto = productoService.crearProducto(producto);
        
        // Retorna un HTTP 201 Created con la URI en las cabeceras y el cuerpo con HATEOAS
        return ResponseEntity
                .created(linkTo(methodOn(ProductoController.class).obtenerPorId(nuevoProducto.getId_producto())).toUri())
                .body(assembler.toModel(nuevoProducto));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> obtenerTodos() {
        log.info("API REST - Petición GET para listar todos los productos activos");
        
        List<EntityModel<ProductoDTO>> productos = productoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (productos.isEmpty()) {
            log.info("La consulta de productos no retornó resultados");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            productos,linkTo(methodOn(ProductoController.class).obtenerTodos()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar producto con ID: {}", id);
        
        ProductoDTO producto = productoService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Producto producto) {
        log.info("API REST - Petición PUT para actualizar el producto con ID: {}", id);
        
        ProductoDTO actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica del producto con ID: {}", id);
        
        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }

}
