package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by sergio on 12/06/2014.
 */
@Entity
public class Evaluacion {
    private Long id;
    private String nombre;
    private List<EvaluacionCriterio> criterios;
    private Date fecha;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToMany
    public List<EvaluacionCriterio> getCriterios() {
        return criterios;
    }

    public void setCriterios(List<EvaluacionCriterio> criterios) {
        this.criterios = criterios;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
