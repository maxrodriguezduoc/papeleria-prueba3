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
@Table(name = "regiones")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRegion;

    @NotBlank(message = "El nombre de la región es obligatorio!")
    @Size(min = 15, max = 50, message = "Nombre de la región debe tener entre 15 a 50 caracteres!")
    @Column(nullable = false, length = 50, unique = true)
    private String nombreRegion;

    @Column(nullable = false)
    private boolean activo;
}