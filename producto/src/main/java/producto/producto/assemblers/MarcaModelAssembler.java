package producto.producto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import producto.producto.controller.v2.MarcaControllerV2;
import producto.producto.dto.MarcaDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MarcaModelAssembler implements RepresentationModelAssembler<MarcaDTO, EntityModel<MarcaDTO>> {

    @Override
    public EntityModel<MarcaDTO> toModel(MarcaDTO marca) {
        return EntityModel.of(marca,
                linkTo(methodOn(MarcaControllerV2.class).obtenerPorId(marca.getId_marcas())).withSelfRel(),
                linkTo(methodOn(MarcaControllerV2.class).actualizar(marca.getId_marcas(), null)).withRel("actualizar"),
                linkTo(methodOn(MarcaControllerV2.class).eliminar(marca.getId_marcas())).withRel("eliminar"),
                linkTo(methodOn(MarcaControllerV2.class).obtenerTodas()).withRel("marcas")
        );
    }
}