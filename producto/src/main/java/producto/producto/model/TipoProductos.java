package producto.producto.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "tipos_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoProductos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTiposProductos;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    private TipoProducto tipoProducto;
}
