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

    @GetMapping
    public CollectionModel<EntityModel<ColaboradorDTO>> listar(){

        log.info("API REST V2 - PETICIÓN PARA LISTAR COLABORADORES ACTIVOS");

        List<EntityModel<ColaboradorDTO>> colaboradores = colaboradorService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(colaboradores,
                linkTo(methodOn(ColaboradorControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<ColaboradorDTO> buscar(@PathVariable Integer id){

        log.info("API REST V2 - PETICIÓN PARA BUSCAR COLABORADOR POR ID: {}", id);

        ColaboradorDTO colaborador = colaboradorService.buscarPorId(id);

        return assembler.toModel(colaborador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ColaboradorDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Colaborador colaborador){

        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR COLABORADOR CON ID: {}", id);

        ColaboradorDTO actualizado = colaboradorService.actualizarColaborador(id, colaborador);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){

        log.warn("API REST V2 - PETICIÓN PARA DESACIVAR COLABORADOR CON ID: {}", id);

        colaboradorService.eliminarColaborador(id);

        return ResponseEntity.ok("COLABORADOR CON ID " + id + " ELIMINADO CON EXITO!");
    }
}