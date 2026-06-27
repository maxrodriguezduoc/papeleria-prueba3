package com.theoffice.ventas.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoExternoDTO {
    private Integer id_producto;
    private String nombre_producto;
    private Integer precio_producto;
    private Integer stock;
    private boolean activo = true;
}
