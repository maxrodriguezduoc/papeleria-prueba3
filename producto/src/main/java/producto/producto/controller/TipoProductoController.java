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
import producto.producto.dto.TipoProductoDTO;
import producto.producto.model.TipoProducto;
import producto.producto.service.TipoProductoService;

@RestController
@RequestMapping("/api/v1/tipoProductos")
@Slf4j

public class TipoProductoController {
    @Autowired
    private TipoProductoService tipoProductoService;

    @PostMapping
    public ResponseEntity<TipoProductoDTO> crear(@Valid @RequestBody TipoProducto tipoProducto) {
        log.info("POST - Crear tipo de producto: {}", tipoProducto.getNombre());
        TipoProductoDTO nuevo = tipoProductoService.crear(tipoProducto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TipoProductoDTO>> obtenerTodos() {
        log.info("GET - Listar tipos de producto activos");
        List<TipoProductoDTO> tipos = tipoProductoService.obtenerActivos();
        if (tipos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoProductoDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("GET - Buscar tipo de producto ID: {}", id);
        return ResponseEntity.ok(tipoProductoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoProductoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody TipoProducto tipoProducto) {
        log.info("PUT - Actualizar tipo de producto ID: {}", id);
        return ResponseEntity.ok(tipoProductoService.actualizar(id, tipoProducto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("DELETE - Eliminar tipo de producto ID: {}", id);
        tipoProductoService.eliminar(id);
        return ResponseEntity.ok("Tipo de producto con ID " + id + " desactivado con éxito.");
    }
}
