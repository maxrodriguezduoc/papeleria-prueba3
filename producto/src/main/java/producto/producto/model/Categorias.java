package producto.producto.model;

import jakarta.persistence.Column;
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
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategorias;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto; // Muchos puentes apuntan a un Producto

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria; // Muchos puentes apuntan a una Categoria

    @Column(nullable = false)
    private boolean activo;


}
