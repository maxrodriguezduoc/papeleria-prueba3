package Ubicacion.Local.dto;

import lombok.Data;

@Data
public class ColaboradorDTO {

    private Integer idColaborador;
    private String nombreColaborador;
    private Integer cargoId;
    private Integer localId;
    private boolean activo;
}