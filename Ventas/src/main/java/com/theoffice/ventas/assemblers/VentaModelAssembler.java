package com.theoffice.ventas.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.theoffice.ventas.DTO.VentaDTO;
import com.theoffice.ventas.controller.v2.VentaControllerV2;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<VentaDTO, EntityModel<VentaDTO>> {
    
    @SuppressWarnings("null")
    @Override
    public EntityModel<VentaDTO> toModel(VentaDTO venta){
        Integer id = venta.getId_venta();

        return EntityModel.of(venta,
                linkTo(methodOn(VentaControllerV2.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(VentaControllerV2.class).obtenerTodas()).withRel("ventas"),
                linkTo(methodOn(VentaControllerV2.class).actualizar(id, null)).withRel("actualizar"),
                linkTo(methodOn(VentaControllerV2.class).eliminar(id)).withRel("eliminar")
        );
    }
}