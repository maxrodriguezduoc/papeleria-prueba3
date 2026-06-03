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
import producto.producto.dto.MarcaDTO;
import producto.producto.model.Marca;
import producto.producto.service.MarcaService;

@RestController
@RequestMapping("/api/v1/marcas")
@Slf4j
public class MarcaController {
    
    @Autowired
    private MarcaService marcaService;

    @PostMapping
    public ResponseEntity<MarcaDTO> crear(@Valid @RequestBody Marca marca) {
        log.info("API REST - POST crear marca: {}", marca.getNombre_marca());
        MarcaDTO nuevaMarca = marcaService.crearMarca(marca);
        return new ResponseEntity<>(nuevaMarca, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MarcaDTO>> obtenerTodas() {
        log.info("API REST - GET listar marcas activas");
        List<MarcaDTO> marcas = marcaService.obtenerTodos();
        if (marcas.isEmpty()) {
            log.info("No se encontraron marcas activas");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - GET buscar marca ID: {}", id);
        return ResponseEntity.ok(marcaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaDTO> actualizar(@PathVariable Integer id,@Valid @RequestBody Marca marca) {
        log.info("API REST - PUT actualizar marca ID: {}", id);
        return ResponseEntity.ok(marcaService.actualizarMarca(id, marca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - DELETE eliminar marca ID: {}", id);
        marcaService.eliminarMarca(id);
        return ResponseEntity.ok("La marca con ID " + id + " ha sido desactivada con éxito.");
    }
}
