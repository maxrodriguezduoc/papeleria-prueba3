package producto.producto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import producto.producto.controller.v2.ColorControllerV2;
import producto.producto.dto.ColorDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ColorModelAssembler implements RepresentationModelAssembler<ColorDTO, EntityModel<ColorDTO>> {

    @Override
    public EntityModel<ColorDTO> toModel(ColorDTO color) {
        return EntityModel.of(color,
                linkTo(methodOn(ColorControllerV2.class).obtenerPorId(color.getId_color())).withSelfRel(),
                linkTo(methodOn(ColorControllerV2.class).actualizar(color.getId_color(), null)).withRel("actualizar"),
                linkTo(methodOn(ColorControllerV2.class).eliminar(color.getId_color())).withRel("eliminar"),
                linkTo(methodOn(ColorControllerV2.class).obtenerTodos()).withRel("colores")
        );
    }
}