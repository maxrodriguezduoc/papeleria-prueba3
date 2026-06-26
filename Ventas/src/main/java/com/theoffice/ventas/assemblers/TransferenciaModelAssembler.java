package com.theoffice.ventas.assemblers;

import com.theoffice.ventas.DTO.TransferenciaDTO;
import com.theoffice.ventas.controller.v2.TransferenciaControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransferenciaModelAssembler implements RepresentationModelAssembler<TransferenciaDTO, EntityModel<TransferenciaDTO>> {

    @Override
    public EntityModel<TransferenciaDTO> toModel(TransferenciaDTO transferenciaDTO) {
        
        Integer id = transferenciaDTO.getIdTransferencia();

        return EntityModel.of(transferenciaDTO,
                linkTo(methodOn(TransferenciaControllerV2.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(TransferenciaControllerV2.class).obtenerTodas()).withRel("transferencias"),
                linkTo(methodOn(TransferenciaControllerV2.class).actualizar(id, null)).withRel("actualizar"),
                linkTo(methodOn(TransferenciaControllerV2.class).eliminar(id)).withRel("eliminar")
        );
    }
}