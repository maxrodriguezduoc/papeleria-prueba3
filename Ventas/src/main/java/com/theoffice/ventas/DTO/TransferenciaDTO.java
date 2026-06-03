package com.theoffice.ventas.DTO;

import lombok.Data;

@Data
public class TransferenciaDTO {
    private Integer idTransferencia;
    private String bancoOrigen;
    private String numeroCuentaOrigen;
    private String bancoDestino;
    private String numeroCuentaDestino;
    private double monto;
    private boolean activo;
}
