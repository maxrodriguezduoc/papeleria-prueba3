package producto.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "productos")
@AllArgsConstructor
@NoArgsConstructor
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProductos;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto; // Conexión con el Producto

    @Column(nullable = false)
    private Integer cantidad; // Cuántas unidades se compraron

    @Column(nullable = false)
    @Positive(message = "el precio debe ser mayor a 0")
    private Integer precioUnitario;

    @Column(nullable = false)
    private boolean activo;

}
