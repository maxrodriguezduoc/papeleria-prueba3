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
@Table(name = "marcas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Marcas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMarcas;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;
}
