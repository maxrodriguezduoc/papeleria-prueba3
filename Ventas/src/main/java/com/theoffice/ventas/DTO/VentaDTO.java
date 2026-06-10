package com.theoffice.ventas.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VentaDTO {
    private Integer id_venta;
    private Integer cantidad;
    private Integer total_venta;
    private LocalDateTime fecha_venta;
    private boolean activo;
    private String nombreProducto;
}
