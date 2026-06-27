package com.theoffice.ventas.assemblers;

import com.theoffice.ventas.DTO.TarjetaDTO;
import com.theoffice.ventas.controller.v2.TarjetaControllerV2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TarjetaModelAssembler implements RepresentationModelAssembler<TarjetaDTO, EntityModel<TarjetaDTO>> {

    @Override
    public EntityModel<TarjetaDTO> toModel(TarjetaDTO tarjetaDTO) {
        
        Integer id = tarjetaDTO.getIdTarjeta();

        return EntityModel.of(tarjetaDTO,
                linkTo(methodOn(TarjetaControllerV2.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(TarjetaControllerV2.class).obtenerTodas()).withRel("tarjetas"),
                linkTo(methodOn(TarjetaControllerV2.class).actualizar(id, null)).withRel("actualizar"),
                linkTo(methodOn(TarjetaControllerV2.class).eliminar(id)).withRel("eliminar")
        );
    }
}