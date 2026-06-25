package Ubicacion.Local.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import Ubicacion.Local.controller.v2.CargoControllerV2;
import Ubicacion.Local.dto.CargoDTO;

@Component
public class CargoModelAssembler implements RepresentationModelAssembler<CargoDTO, EntityModel<CargoDTO>>{

    @Override
    public EntityModel<CargoDTO> toModel(CargoDTO cargo){
        return EntityModel.of(cargo,
                linkTo(methodOn(CargoControllerV2.class)
                .buscar(cargo.getIdCargo()))
                .withSelfRel(),

                linkTo(methodOn(CargoControllerV2.class)
                .listar())
                .withRel("cargos"),

                linkTo(methodOn(CargoControllerV2.class)
                .actualizar(cargo.getIdCargo(), null))
                .withRel("actualizar"),

                linkTo(methodOn(CargoControllerV2.class)
                .eliminar(cargo.getIdCargo()))
                .withRel("eliminar")
        );
    }
}