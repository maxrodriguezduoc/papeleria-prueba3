package Ubicacion.Local.controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import Ubicacion.Local.assembler.RegionModelAssembler;
import Ubicacion.Local.dto.RegionDTO;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.service.RegionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/regiones")
public class RegionControllerV2 {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<RegionDTO>> listar(){

        log.info("API REST V2 - PETICIÓN PARA LISTAR REGIONES ACTIVOS");

        List<EntityModel<RegionDTO>> regiones = regionService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(regiones,
                linkTo(methodOn(RegionControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<RegionDTO> buscar(@PathVariable Integer id){

        log.info("API REST V2 - PETICIÓN PARA BUSCAR REGION POR ID: {}", id);

        RegionDTO region = regionService.buscarPorId(id);

        return assembler.toModel(region);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<RegionDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Region region){

        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR REGION CON ID: {}", id);

        RegionDTO actualizado = regionService.actualizarRegion(id, region);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){

        log.warn("API REST V2 - PETICIÓN PARA DESACIVAR REGION CON ID: {}", id);

        regionService.eliminarRegion(id);

        return ResponseEntity.ok("REGION CON ID " + id + " ELIMINADO CON EXITO!");
    }
}