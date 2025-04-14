package gtics.l2_20223209_implementacion.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity

@Table(name="auto")
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idauto", nullable = false)
    private Integer idAuto;
    @Column(name = "modelo", nullable = false, length = 45)
    private String modelo;
    @Column(name = "color", nullable = false, length = 45)
    private String color;
    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;
    @Column(name = "costo_por_dia", nullable = false)
    private float costoPorDia;
    @ManyToOne
    @JoinColumn(name="sede_idsede")
    private Sede sede;
}
