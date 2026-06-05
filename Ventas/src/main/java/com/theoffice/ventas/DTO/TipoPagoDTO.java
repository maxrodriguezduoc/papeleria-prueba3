package com.theoffice.ventas.DTO;

import lombok.Data;

@Data
public class TipoPagoDTO {
    private Integer idTipoPago;
    private String formaPago;
    private boolean activo;
}
