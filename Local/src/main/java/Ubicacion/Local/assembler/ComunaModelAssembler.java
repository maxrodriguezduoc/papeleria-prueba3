package Ubicacion.Local.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import Ubicacion.Local.controller.v2.ComunaControllerV2;
import Ubicacion.Local.dto.ComunaDTO;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler <ComunaDTO, EntityModel<ComunaDTO>>{

    @Override
    public EntityModel<ComunaDTO> toModel(ComunaDTO comuna){
        return EntityModel.of(comuna,
                linkTo(methodOn(ComunaControllerV2.class)
                .buscar(comuna.getIdComuna()))
                .withSelfRel(),

                linkTo(methodOn(ComunaControllerV2.class)
                .listar())
                .withRel("comunas"),

                linkTo(methodOn(ComunaControllerV2.class)
                .actualizar(comuna.getIdComuna(), null))
                .withRel("actualizar"),

                linkTo(methodOn(ComunaControllerV2.class)
                .eliminar(comuna.getIdComuna()))
                .withRel("eliminar")
        );
    } 
}