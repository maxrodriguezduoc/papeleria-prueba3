package com.cliente.cliente.model;

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

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "Formato de RUT inválido")
    private String rut;

    @Column(nullable = false, length = 100 )
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 10, max = 100, message = "Nombre completo debe tener entre 10 y 100 caracteres")
    private String nombreCompleto;

    @Column(nullable = false)
    private boolean activo = true;
}

