package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * Created by Usuario on 09/09/2014.
 */
@Entity
@NamedQuery( name = "EvaluacionRespuesta.list", query = "select distinct er from EvaluacionRespuesta er group by profesor " )
public class EvaluacionRespuesta {

    private Long id;
    private String respuesta;

    private EvaluacionCriterio criterio;

    private String profesor;
    private String materia;

    private transient int contRow = 0;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public void setRespuesta( String respuesta ) {
        this.respuesta = respuesta;
    }

    @ManyToOne( cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    public EvaluacionCriterio getCriterio() {
        return criterio;
    }

    public void setCriterio( EvaluacionCriterio criterio ) {
        this.criterio = criterio;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor( String profesor ) {
        this.profesor = profesor;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria( String materia ) {
        this.materia = materia;
    }

    @Transient
    public int getContRow() {
        return ++contRow;
    }

    public void setContRow( int contRow ) {
        this.contRow = contRow;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
