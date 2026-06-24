package Ubicacion.Local.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import Ubicacion.Local.controller.v2.LocalControllerV2;
import Ubicacion.Local.dto.LocalDTO;

@Component
public class LocalModelAssembler implements RepresentationModelAssembler<LocalDTO, EntityModel<LocalDTO>>{

    @Override
    public EntityModel<LocalDTO> toModel(LocalDTO local){
        return EntityModel.of(local,
                linkTo(methodOn(LocalControllerV2.class)
                .buscar(local.getIdLocal()))
                .withSelfRel(),

                linkTo(methodOn(LocalControllerV2.class)
                .listar())
                .withRel("locales"),

                linkTo(methodOn(LocalControllerV2.class)
                .actualizar(local.getIdLocal(), null))
                .withRel("actualizar"),

                linkTo(methodOn(LocalControllerV2.class)
                .eliminar(local.getIdLocal()))
                .withRel("eliminar")
        );
    } 
}