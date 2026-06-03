package Ubicacion.Local.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "locales")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLocal;

    @NotBlank (message = "El nombre de local es obligatorio!")
    @Size(min = 5, max = 50, message = "El nombre de local debe tener entre 5 a 50 caracteres!")
    @Column(nullable = false, length = 50)
    private String nombreLocal;

    @NotBlank (message = "La dirección de local es obligatorio!")
    @Size(min = 15, max = 60, message = "La dirección debe tener entre 15 a 60 caracteres!")
    @Column(nullable = false, length = 60)
    private String direccion;

    @Column(nullable = false)
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_comuna", nullable = false)
    private Comuna comuna;

    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Colaboradores> colaboradores = new ArrayList<>();
}