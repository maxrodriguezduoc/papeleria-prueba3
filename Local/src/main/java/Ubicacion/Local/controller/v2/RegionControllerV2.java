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

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> crear(@Valid @RequestBody Region region) {
        log.info("API REST V2 - PETICIÓN PARA POSTEAR REGIONES: '{}'", region.getNombreRegion());
        
        RegionDTO nuevaRegion = regionService.guardarRegion(region);
        
        return ResponseEntity
                .created(linkTo(methodOn(RegionControllerV2.class).buscar(nuevaRegion.getIdRegion())).toUri())
                .body(assembler.toModel(nuevaRegion));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RegionDTO>>> listar() {
        log.info("API REST V2 - PETICIÓN PARA LISTAR REGIONES");
        
        List<EntityModel<RegionDTO>> regiones = regionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (regiones.isEmpty()) {
            log.info("LA CONSULTA DE REGIONES NO RETORNO REGISTROS ACTIVOS");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            regiones, linkTo(methodOn(RegionControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> buscar(@PathVariable Integer id) {
        log.info("API REST V2 - PETICIÓN PARA BUSCAR REGION CON ID: {}", id);
        
        RegionDTO region = regionService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(region));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Region region) {
        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR REGION CON ID: {}", id);
        
        RegionDTO actualizada = regionService.actualizarRegion(id, region);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - PETICIÓN PARA ELIMINAR REGION CON ID: {}", id);
        
        regionService.eliminarRegion(id);
        return ResponseEntity.noContent().build(); 
    }
}