package gtics.l2_20223209_implementacion.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity

@Table(name="seguro")
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idseguro", nullable = false)
    private Integer idSeguro;
    @Column(name = "empresa_aseguradora", nullable = false, length = 45)
    private String empresaAseguradora;
    @Column(name = "cobertura_maxima", nullable = false)
    private double coberturaMaxima;
    @Column(name = "tarifa", nullable = false)
    private double tarifa;

}
