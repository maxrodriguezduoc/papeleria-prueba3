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

import Ubicacion.Local.assembler.ColaboradorModelAssembler;
import Ubicacion.Local.dto.ColaboradorDTO;
import Ubicacion.Local.model.Colaborador;
import Ubicacion.Local.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/colaboradores")
public class ColaboradorControllerV2 {

    @Autowired
    private ColaboradorService colaboradorService;

    @Autowired
    private ColaboradorModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ColaboradorDTO>> crear(@Valid @RequestBody Colaborador colaborador) {
        log.info("API REST V2 - PETICIÓN PARA POSTEAR COLABORADORES: '{}'", colaborador.getNombreColaborador());
        
        ColaboradorDTO nuevoColaborador = colaboradorService.guardarColaborador(colaborador);
        
        return ResponseEntity
                .created(linkTo(methodOn(ColaboradorControllerV2.class).buscar(nuevoColaborador.getIdColaborador())).toUri())
                .body(assembler.toModel(nuevoColaborador));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ColaboradorDTO>>> listar() {
        log.info("API REST V2 - PETICIÓN PARA LISTAR COLABORADORES");
        
        List<EntityModel<ColaboradorDTO>> colaboradores = colaboradorService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (colaboradores.isEmpty()) {
            log.info("LA CONSULTA DE COLABORADORES NO RETORNO REGISTROS ACTIVOS");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            colaboradores, linkTo(methodOn(ColaboradorControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ColaboradorDTO>> buscar(@PathVariable Integer id) {
        log.info("API REST V2 - PETICIÓN PARA BUSCAR COLABORADOR CON ID: {}", id);
        
        ColaboradorDTO colaborador = colaboradorService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(colaborador));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ColaboradorDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Colaborador colaborador) {
        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR COLABORADOR CON ID: {}", id);
        
        ColaboradorDTO actualizada = colaboradorService.actualizarColaborador(id, colaborador);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - PETICIÓN PARA ELIMINAR COLABORADOR CON ID: {}", id);
        
        colaboradorService.eliminarColaborador(id);
        return ResponseEntity.noContent().build(); 
    }
}