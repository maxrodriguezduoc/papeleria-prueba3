package Ubicacion.Local.dto;

import lombok.Data;

@Data
public class ComunaDTO {

    private Integer idComuna;
    private String nombreComuna;
    private String codigoPostal;
    private Integer regionId;
    private boolean activo;
}