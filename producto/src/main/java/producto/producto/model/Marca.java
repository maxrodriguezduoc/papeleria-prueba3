package producto.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "marca")

public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_marcas;

    @NotBlank (message = "El nombre  de la marca es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre  de la marca debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre_marca;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
