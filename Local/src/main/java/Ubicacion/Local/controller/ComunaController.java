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

import Ubicacion.Local.dto.ComunaDTO;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.service.ComunaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/comunas")
@Slf4j
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @PostMapping
    public ResponseEntity<ComunaDTO> crear(@Valid @RequestBody Comuna comuna) {
        log.info("API REST - Petición POST para crear una comuna con nombre: {}", comuna.getNombreComuna());
        
        ComunaDTO nuevaComuna = comunaService.guardarComuna(comuna);
        return new ResponseEntity<>(nuevaComuna, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ComunaDTO>> obtenerTodos() {
        log.info("API REST - Petición GET para listar todas las comunas activas");
        
        List<ComunaDTO> comunas = comunaService.obtenerTodos();
        
        if (comunas.isEmpty()) {
            log.info("La consulta de comunas no retornó resultados");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(comunas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("API REST - Petición GET para buscar comuna con ID: {}", id);
        
        ComunaDTO comuna = comunaService.buscarPorId(id);
        return ResponseEntity.ok(comuna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        log.info("API REST - Petición PUT para actualizar la comuna con ID: {}", id);
        
        ComunaDTO actualizada = comunaService.actualizarComuna(id, comuna);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        log.warn("API REST - Petición DELETE para baja lógica de la comuna con ID: {}", id);
        
        comunaService.eliminarComuna(id);
        return ResponseEntity.ok("La comuna con ID " + id + " ha sido desactivada con éxito de la papelería.");
    }
}