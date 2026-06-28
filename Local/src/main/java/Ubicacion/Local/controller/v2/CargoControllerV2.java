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

import Ubicacion.Local.assembler.CargoModelAssembler;
import Ubicacion.Local.dto.CargoDTO;
import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.service.CargoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/cargos")
public class CargoControllerV2 {

    @Autowired
    private CargoService cargoService;

    @Autowired
    private CargoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CargoDTO>> crear(@Valid @RequestBody Cargo cargo) {
        log.info("API REST V2 - PETICIÓN PARA POSTEAR CARGOS: '{}'", cargo.getNombreCargo());
        
        CargoDTO nuevoCargo = cargoService.guardarCargo(cargo);
        
        return ResponseEntity
                .created(linkTo(methodOn(CargoControllerV2.class).buscar(nuevoCargo.getIdCargo())).toUri())
                .body(assembler.toModel(nuevoCargo));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<CargoDTO>>> listar() {
        log.info("API REST V2 - PETICIÓN PARA LISTAR CARGOS");
        
        List<EntityModel<CargoDTO>> cargos = cargoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        if (cargos.isEmpty()) {
            log.info("LA CONSULTA DE CARGOS NO RETORNO REGISTROS ACTIVOS");
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
            cargos, linkTo(methodOn(CargoControllerV2.class).listar()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CargoDTO>> buscar(@PathVariable Integer id) {
        log.info("API REST V2 - PETICIÓN PARA BUSCAR CARGO CON ID: {}", id);
        
        CargoDTO cargo = cargoService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(cargo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CargoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Cargo cargo) {
        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR CARGO CON ID: {}", id);
        
        CargoDTO actualizada = cargoService.actualizarCargo(id, cargo);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("API REST V2 - PETICIÓN PARA ELIMINAR CARGO CON ID: {}", id);
        
        cargoService.eliminarCargo(id);
        return ResponseEntity.noContent().build(); 
    }
}