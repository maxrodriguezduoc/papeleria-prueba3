package producto.producto.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import producto.producto.controller.CategoriaController;
import producto.producto.dto.CategoriaDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {

    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).obtenerPorId(categoria.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).actualizar(categoria.getIdCategoria(), null)).withRel("actualizar"),
                linkTo(methodOn(CategoriaController.class).eliminar(categoria.getIdCategoria())).withRel("eliminar"),
                linkTo(methodOn(CategoriaController.class).obtenerTodas()).withRel("categorias")
        );
    }
}