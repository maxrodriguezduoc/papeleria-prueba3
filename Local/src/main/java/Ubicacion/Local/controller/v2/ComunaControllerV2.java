package Ubicacion.Local.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/v2/comunass")
public class ComunaControllerV2 {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<ComunaDTO>> listar(){

        log.info("API REST V2 - PETICIÓN PARA LISTAR COMUNAS ACTIVOS");

        List<EntityModel<ComunaDTO>> comunas = comunaService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(comunas,
                linkTo(methodOn(ComunaControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<ComunaDTO> buscar(@PathVariable Integer id){

        log.info("API REST V2 - PETICIÓN PARA BUSCAR COMUNA POR ID: {}", id);

        ComunaDTO comuna = comunaService.buscarPorId(id);

        return assembler.toModel(comuna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ComunaDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Comuna comuna){

        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR COMUNA CON ID: {}", id);

        ComunaDTO actualizado = comunaService.actualizarComuna(id, comuna);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){

        log.warn("API REST V2 - PETICIÓN PARA DESACIVAR COMUNA CON ID: {}", id);

        comunaService.eliminarComuna(id);

        return ResponseEntity.ok("COMUNA CON ID " + id + " ELIMINADO CON EXITO!");
    }
}