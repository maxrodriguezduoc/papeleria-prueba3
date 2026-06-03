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
import producto.producto.dto.CategoriaDTO;
import producto.producto.model.Categoria;
import producto.producto.service.CategoriaService;

@RestController
@RequestMapping("/api/v1/categorias")
@Slf4j

public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@Valid @RequestBody Categoria categoria) {
        log.info("API REST - Petición POST para crear una nueva categoría: '{}'", categoria.getNombre());
        
        CategoriaDTO nuevaCategoria = categoriaService.crear(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerTodas() {
        log.info("API REST - Petición GET para listar todas las categorías activas");
        
        List<CategoriaDTO> categorias = categoriaService.obtenerTodas();
        
        if (categorias.isEmpty()) {
            log.info("La consulta de categorías no retornó registros activos");
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }
        
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar la categoría con ID: {}", id);
        
        CategoriaDTO categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Categoria categoria) {
        log.info("API REST - Petición PUT para actualizar la categoría con ID: {}", id);
        
        CategoriaDTO actualizada = categoriaService.actualizar(id, categoria);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica de la categoría con ID: {}", id);
        
        categoriaService.eliminar(id);
        return ResponseEntity.ok("La categoría con ID " + id + " ha sido desactivada con éxito.");
    }
}
