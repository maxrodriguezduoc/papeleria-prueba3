package com.theoffice.ventas.assemblers;

import com.theoffice.ventas.DTO.PagoDTO;
import com.theoffice.ventas.controller.v2.PagoControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoDTO, EntityModel<PagoDTO>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<PagoDTO> toModel(PagoDTO pagoDTO) {

        Integer id = pagoDTO.getIdPago(); 

        return EntityModel.of(pagoDTO,
                linkTo(methodOn(PagoControllerV2.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withRel("pagos"),
                linkTo(methodOn(PagoControllerV2.class).actualizar(id, null)).withRel("actualizar"),
                linkTo(methodOn(PagoControllerV2.class).eliminar(id)).withRel("eliminar")
        );
    }
}