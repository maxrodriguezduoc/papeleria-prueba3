package producto.producto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import producto.producto.dto.ProductoDTO;
import producto.producto.model.Producto;
import producto.producto.service.ProductoService;
@RestController
@RequestMapping("/api/v1/productos")
@Slf4j

public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody Producto producto) {
        log.info("API REST - Petición POST para crear un producto con nombre: {}", producto.getNombre_producto());
        
        ProductoDTO nuevoProducto = productoService.crearProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        log.info("API REST - Petición GET para listar todos los productos activos");
        
        List<ProductoDTO> productos = productoService.obtenerTodos();
        
        if (productos.isEmpty()) {
            log.info("La consulta de productos no retornó resultados");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar producto con ID: {}", id);
        
        ProductoDTO producto = productoService.buscarPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Producto producto) {
        log.info("API REST - Petición PUT para actualizar el producto con ID: {}", id);
        
        ProductoDTO actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica del producto con ID: {}", id);
        
        productoService.eliminar(id);
        return ResponseEntity.ok("El producto con ID " + id + " ha sido desactivado con éxito de la papelería.");
    }
}
