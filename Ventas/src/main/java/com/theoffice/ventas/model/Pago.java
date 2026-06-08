package com.theoffice.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta; // Relación con la Venta

    @NotNull(message = "La forma de pago es obligatoria")
    @Column(name = "forma_pago", nullable = false, length = 30)
    private String formaPago; // Guarda: 'EFECTIVO', 'TRANSFERENCIA', etc.

    @ManyToOne
    @JoinColumn(name = "id_tarjeta", nullable = true)
    private Tarjeta tarjeta; // Solo se llena si formaPago es TARJETA_...

    @ManyToOne
    @JoinColumn(name = "id_transferencia", nullable = true)
    private Transferencia transferencia; // Solo se llena si formaPago es TRANSFERENCIA

    @Column(nullable = false)
    @NotNull(message = "El monto a pagar es obligatorio")
    @Positive(message = "El monto de pago debe ser mayor a cero")
    private Integer monto;

    @Column(nullable = false)
    private boolean activo = true;
}