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

import Ubicacion.Local.assembler.LocalModelAssembler;
import Ubicacion.Local.dto.LocalDTO;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.service.LocalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/locales")
public class LocalControllerV2 {

    @Autowired
    private LocalService localService;

    @Autowired
    private LocalModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LocalDTO>> crear(@Valid @RequestBody Local local) {
        log.info("API REST V2 - PETICIÓN PARA POSTEAR LOCALES: '{}'", local.getNombreLocal());
        
        LocalDTO nuevoLocal = localService.guardarLocal(local);
        
        return ResponseEntity
                .created(linkTo(methodOn(LocalControllerV2.class).buscar(nuevoLocal.getIdLocal())).toUri())
                .body(assembler.toModel(nuevoLocal));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<LocalDTO>>> listar() {
        log.info("API REST V2 - PETICIÓN PARA LISTAR LOCALES");
        
        List<EntityModel<LocalDTO>> locales = localService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (locales.isEmpty()) {
            log.info("LA CONSULTA DE LOCALES NO RETORNO REGISTROS ACTIVOS");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            locales, linkTo(methodOn(ComunaControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LocalDTO>> buscar(@PathVariable Integer id) {
        log.info("API REST V2 - PETICIÓN PARA BUSCAR LOCAL CON ID: {}", id);
        
        LocalDTO local = localService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(local ));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LocalDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Local local) {
        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR LOCAL CON ID: {}", id);
        
        LocalDTO actualizada = localService.actualizarLocal(id, local);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - PETICIÓN PARA ELIMINAR LOCAL CON ID: {}", id);
        
        localService.eliminarLocal(id);
        return ResponseEntity.noContent().build(); 
    }
}