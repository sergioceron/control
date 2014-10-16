package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Usuario on 28/09/2014.
 */
@Entity
public class Periodo {
    private Long id;
    private int anyo;
    private String semestre;
    private Date inicio;
    private Date termino;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    @Temporal(TemporalType.DATE)
    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    @Temporal(TemporalType.DATE)
    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    @Override
    public String toString() {
        return anyo + "/" + semestre;
    }
}
