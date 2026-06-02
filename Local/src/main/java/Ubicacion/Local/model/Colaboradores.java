package Ubicacion.Local.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "colaboradores")
public class Colaboradores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idColaboradores;

    @ManyToOne
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;

    @ManyToOne
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;

    @Column(nullable = false)
    private boolean activo;
}