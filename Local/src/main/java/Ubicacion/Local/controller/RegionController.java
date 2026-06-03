package Ubicacion.Local.controller;

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

import Ubicacion.Local.dto.RegionDTO;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.service.RegionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/regiones")
@Slf4j
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping
    public ResponseEntity<RegionDTO> crear(@Valid @RequestBody Region region) {
        log.info("API REST - Petición POST para crear una región con nombre: {}", region.getNombreRegion());
        
        RegionDTO nuevaRegion = regionService.guardarRegion(region);
        return new ResponseEntity<>(nuevaRegion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodos() {
        log.info("API REST - Petición GET para listar todas las regiones activas");
        
        List<RegionDTO> regiones = regionService.obtenerTodos();
        
        if (regiones.isEmpty()) {
            log.info("La consulta de regiones no retornó resultados");
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }
        
        return ResponseEntity.ok(regiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar región con ID: {}", id);
        
        RegionDTO region = regionService.buscarPorId(id);
        return ResponseEntity.ok(region);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Region region) {
        log.info("API REST - Petición PUT para actualizar la región con ID: {}", id);
        
        RegionDTO actualizada = regionService.actualizarRegion(id, region);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica de la región con ID: {}", id);
        
        regionService.eliminarRegion(id);
        return ResponseEntity.ok("La región con ID " + id + " ha sido desactivada con éxito de la papelería.");
    }
}