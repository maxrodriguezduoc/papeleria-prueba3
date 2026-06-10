package com.theoffice.ventas.DTO;
import lombok.Data;

@Data
public class ProductoDTO {
    private Integer id_producto;
    private String nombre_producto;
    private Integer precio_producto;
    private Integer stock;
    private boolean activo = true;
}
