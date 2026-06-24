package Ubicacion.Local.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

    @PostMapping
    public ResponseEntity<EntityModel<LocalDTO>> crear(@Valid @RequestBody Local local){

        log.info("API REST V2 - PETICIÓN PARA POSTEAR LOCAL CON NOMBRE: {}", local.getNombreLocal());

        LocalDTO nuevoLocal = localService.guardarLocal(local);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(nuevoLocal));
    }

    @GetMapping
    public CollectionModel<EntityModel<LocalDTO>> listar(){

        log.info("API REST V2 - PETICIÓN PARA LISTAR LOCALES ACTIVOS");

        List<EntityModel<LocalDTO>> locales = localService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(locales,
                linkTo(methodOn(LocalControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<LocalDTO> buscar(@PathVariable Integer id){

        log.info("API REST V2 - PETICIÓN PARA BUSCAR LOCAL POR ID: {}", id);

        LocalDTO local = localService.buscarPorId(id);

        return assembler.toModel(local);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<LocalDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Local local){

        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR LOCAL CON ID: {}", id);

        LocalDTO actualizado = localService.actualizarLocal(id, local);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){

        log.warn("API REST V2 - PETICIÓN PARA DESACIVAR LOCAL CON ID: {}", id);

        localService.eliminarLocal(id);

        return ResponseEntity.ok("LOCAL CON ID " + id + " ELIMINADO CON EXITO!");
    }
}