package Ubicacion.Local.controller.v2;

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

import Ubicacion.Local.assembler.ComunaModelAssembler;
import Ubicacion.Local.dto.ComunaDTO;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.service.ComunaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/comunas")
public class ComunaControllerV2 {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> crear(@Valid @RequestBody Comuna comuna) {
        log.info("API REST V2 - PETICIÓN PARA POSTEAR COMUNAS: '{}'", comuna.getNombreComuna());
        
        ComunaDTO nuevaComuna = comunaService.guardarComuna(comuna);
        
        return ResponseEntity
                .created(linkTo(methodOn(ComunaControllerV2.class).buscar(nuevaComuna.getIdComuna())).toUri())
                .body(assembler.toModel(nuevaComuna));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ComunaDTO>>> listar() {
        log.info("API REST V2 - PETICIÓN PARA LISTAR COMUNAS");
        
        List<EntityModel<ComunaDTO>> comunas = comunaService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (comunas.isEmpty()) {
            log.info("LA CONSULTA DE COMUNAS NO RETORNO REGISTROS ACTIVOS");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            comunas, linkTo(methodOn(ComunaControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> buscar(@PathVariable Integer id) {
        log.info("API REST V2 - PETICIÓN PARA BUSCAR COMUNA CON ID: {}", id);
        
        ComunaDTO comuna = comunaService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(comuna));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR COMUNA CON ID: {}", id);
        
        ComunaDTO actualizada = comunaService.actualizarComuna(id, comuna);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - PETICIÓN PARA ELIMINAR COMUNA CON ID: {}", id);
        
        comunaService.eliminarComuna(id);
        return ResponseEntity.noContent().build(); 
    }
}