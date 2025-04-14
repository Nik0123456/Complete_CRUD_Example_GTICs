package gtics.l2_20223209_implementacion.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity

@Table(name="sede")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsede", nullable = false)
    private Integer idSede;
    @Column(name = "distrito", nullable = false, length = 45)
    private String distrito;
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;
}
