package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sergio on 12/06/2014.
 */
@Entity
public class Evaluacion {
    private Long id;
    private Alumno alumno;
    private Curso curso;
    private Date fecha;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    @ManyToOne
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
