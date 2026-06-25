package Ubicacion.Local.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import Ubicacion.Local.controller.v2.ColaboradorControllerV2;
import Ubicacion.Local.dto.ColaboradorDTO;

@Component
public class ColaboradorModelAssembler implements RepresentationModelAssembler<ColaboradorDTO, EntityModel<ColaboradorDTO>>{

    @Override
    public EntityModel<ColaboradorDTO> toModel(ColaboradorDTO colaborador){
        return EntityModel.of(colaborador,
                linkTo(methodOn(ColaboradorControllerV2.class)
                .buscar(colaborador.getIdColaborador()))
                .withSelfRel(),

                linkTo(methodOn(ColaboradorControllerV2.class)
                .listar())
                .withRel("colaboradores"),

                linkTo(methodOn(ColaboradorControllerV2.class)
                .actualizar(colaborador.getIdColaborador(), null))
                .withRel("actualizar"),

                linkTo(methodOn(ColaboradorControllerV2.class)
                .eliminar(colaborador.getIdColaborador()))
                .withRel("eliminar")
        );
    }
}