package Ubicacion.Local.model;

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
@Table(name = "cargos")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCargo;

    @NotBlank (message = "Nombre de cargo es obligatorio!")
    @Size(min = 5, max = 50, message = "Nombre del cargo debe tener entre 5 a 50 caracteres!")
    @Column(nullable = false, length = 50)
    private String nombreCargo;

    @Column(nullable = false)
    private boolean activo;
}