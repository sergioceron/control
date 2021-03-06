package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @Transient
    public int getSemanas(){
        /*Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(inicio);
        c2.setTime(termino);
        int startWeek = c1.get(Calendar.WEEK_OF_YEAR);
        int endWeek = c2.get(Calendar.WEEK_OF_YEAR);

        return endWeek - startWeek;*/
	    return 18;
    }

    @Override
    public String toString() {
        return semestre + "" + (anyo - 2000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Periodo periodo = (Periodo) o;

        if (anyo != periodo.anyo) return false;
        if (semestre != null ? !semestre.equals(periodo.semestre) : periodo.semestre != null) return false;

        return true;
    }

}
