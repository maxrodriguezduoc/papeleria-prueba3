package com.theoffice.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transferencias")
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTransferencia;

    @Column(nullable = false)
    @NotBlank(message = "El banco de origen es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del banco de origen debe tener entre 3 y 100 caracteres")
    private String bancoOrigen;

    @Column(nullable = false)
    @NotBlank(message = "El número de cuenta de origen es obligatorio")
    @Size(min = 10, max = 20, message = "El número de cuenta de origen debe tener entre 10 y 20 caracteres")
    private String numeroCuentaOrigen;

    @Column(nullable = false)
    @NotBlank(message = "El banco de destino es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del banco de destino debe tener entre 3 y 100 caracteres")
    private String bancoDestino;

    @Column(nullable = false)
    @NotBlank(message = "El número de cuenta de destino es obligatorio")
    @Size(min = 10, max = 20, message = "El número de cuenta de destino debe tener entre 10 y 20 caracteres")
    private String numeroCuentaDestino;

    @Column(nullable = false)
    @Positive(message = "El monto debe ser mayor a 0")
    private Integer monto;

    @Column(nullable = false)
    private boolean activo;
}
