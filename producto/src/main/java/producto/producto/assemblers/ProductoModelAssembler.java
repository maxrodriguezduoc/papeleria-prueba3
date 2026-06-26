package producto.producto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import producto.producto.controller.v2.ProductoControllerV2;
import producto.producto.dto.ProductoDTO;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>>{
    @Override
    @SuppressWarnings("null")
    public EntityModel<ProductoDTO> toModel(ProductoDTO producto) {
        
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControllerV2.class).obtenerPorId(producto.getId_producto())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).actualizar(producto.getId_producto(), null)).withRel("actualizar"),
                linkTo(methodOn(ProductoControllerV2.class).eliminar(producto.getId_producto())).withRel("eliminar"),
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos")
        );
    }

}
