package com.theoffice.ventas.model;

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
@Table(name = "tipos_pago")
public class TipoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoPago;

    @Column(nullable = false)
    @Size(min = 3, max = 50, message = "El nombre del tipo de pago debe tener entre 3 y 50 caracteres")
    @NotBlank(message = "El nombre del tipo de pago es obligatorio")
    private String formaPago;

    @Column(nullable = false)
    private boolean activo;
}
