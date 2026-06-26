package com.cliente.cliente.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.cliente.cliente.controller.v2.ClienteControllerV2;
import com.cliente.cliente.dto.ClienteDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {

    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(cliente.getIdCliente())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).actualizar(cliente.getIdCliente(), null)).withRel("actualizar"),
                linkTo(methodOn(ClienteControllerV2.class).eliminar(cliente.getIdCliente())).withRel("eliminar"),
                linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("clientes")
        );
    }
}