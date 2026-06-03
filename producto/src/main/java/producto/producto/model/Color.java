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
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_color;

    @NotBlank(message = "El nombre  del color es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del color debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre_color;
    
    @Column(name = "activo", nullable = false)
    private boolean activo = true;

}
