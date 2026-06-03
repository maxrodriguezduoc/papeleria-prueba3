package producto.producto.dto;

import lombok.Data;

@Data
public class TipoProductoDTO {
    private Integer id_tipo_producto;
    private String nombre;
    private Integer precio_producto;
    private Integer stock;
    private boolean activo = true;
}
