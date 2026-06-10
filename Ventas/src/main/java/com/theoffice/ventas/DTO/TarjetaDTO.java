package com.theoffice.ventas.DTO;

import lombok.Data;

@Data
public class TarjetaDTO {
    private Integer idTarjeta;
    private String tipoTarjeta;
    private String nombreBanco;
    private String ultimosCuatro;
    private String nombreTitular;
    private boolean activo;
}
