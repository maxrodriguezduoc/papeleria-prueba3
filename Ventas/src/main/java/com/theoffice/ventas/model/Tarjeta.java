package com.theoffice.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tarjetas")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTarjeta;

    @Column(nullable = false)
    private boolean esDebito; // true para débito, false para crédito

    @Column(nullable = false)
    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Size(min = 13, max = 19, message = "El número de tarjeta debe tener entre 13 y 19 caracteres")
    private String numeroTarjeta;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del banco es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del banco debe tener entre 3 y 50 caracteres")
    private String nombreBanco;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del titular debe tener entre 3 y 100 caracteres")
    private String nombreTitular;

    @Column(nullable = false)
    @NotBlank(message = "La fecha de expiración es obligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "El formato de fecha debe ser MM/YY")
    private String fechaExpiracion;

    @Column(nullable = false)
    @NotBlank(message = "El CVV es obligatorio")
    @Size(min = 3, max = 3, message = "El CVV debe tener 3 dígitos")
    private String cvv;

    @Column(nullable = false)
    private boolean activo;
}
