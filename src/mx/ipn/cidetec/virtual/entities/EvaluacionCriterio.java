package mx.ipn.cidetec.virtual.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by root on 20/09/15.
 */
@Entity
public class EvaluacionCriterio {
    private Long id;
    private String pregunta;
    private String criterio;
    private char identificador;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio( String criterio ) {
        this.criterio = criterio;
    }

    public char getIdentificador() {
        return identificador;
    }

    public void setIdentificador( char identificador ) {
        this.identificador = identificador;
    }
}
