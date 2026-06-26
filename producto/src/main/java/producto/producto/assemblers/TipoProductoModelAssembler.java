package producto.producto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import producto.producto.controller.v2.TipoProductoControllerV2;
import producto.producto.dto.TipoProductoDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoProductoModelAssembler implements RepresentationModelAssembler<TipoProductoDTO, EntityModel<TipoProductoDTO>> {

    @Override
    public EntityModel<TipoProductoDTO> toModel(TipoProductoDTO tipoProducto) {
        return EntityModel.of(tipoProducto,
                linkTo(methodOn(TipoProductoControllerV2.class).obtenerPorId(tipoProducto.getId_tipo_producto())).withSelfRel(),
                linkTo(methodOn(TipoProductoControllerV2.class).actualizar(tipoProducto.getId_tipo_producto(), null)).withRel("actualizar"),
                linkTo(methodOn(TipoProductoControllerV2.class).eliminar(tipoProducto.getId_tipo_producto())).withRel("eliminar"),
                linkTo(methodOn(TipoProductoControllerV2.class).obtenerTodos()).withRel("tipoProductos")
        );
    }
}