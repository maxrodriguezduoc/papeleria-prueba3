package Ubicacion.Local.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import Ubicacion.Local.controller.v2.RegionControllerV2;
import Ubicacion.Local.dto.RegionDTO;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<RegionDTO, EntityModel<RegionDTO>>{

    @Override
    public EntityModel<RegionDTO> toModel(RegionDTO region){
        return EntityModel.of(region,
                linkTo(methodOn(RegionControllerV2.class)
                .buscar(region.getIdRegion()))
                .withSelfRel(),

                linkTo(methodOn(RegionControllerV2.class)
                .listar())
                .withRel("regiones"),

                linkTo(methodOn(RegionControllerV2.class)
                .actualizar(region.getIdRegion(), null))
                .withRel("actualizar"),

                linkTo(methodOn(RegionControllerV2.class)
                .eliminar(region.getIdRegion()))
                .withRel("eliminar")
        );
    } 
}