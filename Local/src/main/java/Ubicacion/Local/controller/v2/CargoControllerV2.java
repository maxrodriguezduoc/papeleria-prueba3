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

    @GetMapping
    public CollectionModel<EntityModel<CargoDTO>> listar(){

        log.info("API REST V2 - PETICIÓN PARA LISTAR CARGOS ACTIVOS");

        List<EntityModel<CargoDTO>> cargos = cargoService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(cargos,
                linkTo(methodOn(CargoControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CargoDTO> buscar(@PathVariable Integer id){

        log.info("API REST V2 - PETICIÓN PARA BUSCAR CARGO POR ID: {}", id);

        CargoDTO cargo = cargoService.buscarPorId(id);

        return assembler.toModel(cargo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CargoDTO>> actualizar(@PathVariable Integer id, @Valid @RequestBody Cargo cargo){

        log.info("API REST V2 - PETICIÓN PARA ACTUALIZAR CARGO CON ID: {}", id);

        CargoDTO actualizado = cargoService.actualizarCargo(id, cargo);

        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id){

        log.warn("API REST V2 - PETICIÓN PARA DESACIVAR CARGO CON ID: {}", id);

        cargoService.eliminarCargo(id);

        return ResponseEntity.ok("CARGO CON ID " + id + " ELIMINADO CON EXITO!");
    }
}