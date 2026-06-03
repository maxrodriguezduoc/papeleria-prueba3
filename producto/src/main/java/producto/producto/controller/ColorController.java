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
import producto.producto.dto.ColorDTO;
import producto.producto.model.Color;
import producto.producto.service.ColorService;

@RestController
@RequestMapping("/api/v1/colores")
@Slf4j
public class ColorController {
    @Autowired
    private ColorService colorService;

    @PostMapping
    public ResponseEntity<ColorDTO> crear(@Valid @RequestBody Color color) {
        log.info("POST - Crear color: {}", color.getNombre_color());
        ColorDTO nuevo = colorService.crear(color);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ColorDTO>> obtenerTodos() {
        log.info("GET - Listar colores activos");
        List<ColorDTO> colores = colorService.obtenerTodos();
        if (colores.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(colores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("GET - Buscar color ID: {}", id);
        return ResponseEntity.ok(colorService.buscarColorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColorDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Color color) {
        log.info("PUT - Actualizar color ID: {}", id);
        return ResponseEntity.ok(colorService.actualizarColor(id, color));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("DELETE - Eliminar color ID: {}", id);
        colorService.eliminarColor(id);
        return ResponseEntity.ok("Color con ID " + id + " desactivado con éxito.");
    }

}
