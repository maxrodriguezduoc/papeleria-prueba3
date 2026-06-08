package com.theoffice.ventas.DTO;

import lombok.Data;

@Data
public class PagoDTO {
    private Integer idPago;
    private String formaPago;
    private Integer monto;
    private boolean activo;
    private Integer idVenta;
}
