package Ubicacion.Local.dto;

import lombok.Data;

@Data
public class LocalDTO {

    private Integer idLocal;
    private String nombreLocal;
    private String direccion;
    private Integer comunaId;
    private boolean activo;
}