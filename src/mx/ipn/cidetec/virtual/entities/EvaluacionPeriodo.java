package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * Created by root on 5/10/15.
 */
@Entity
@Table (name="EvaluacionPeriodo")
public class EvaluacionPeriodo {

    private long id;
    private boolean activacion = false;
    private Alumno alumno;
    private Curso curso;
    private boolean excepcion;
    private Periodo periodo;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActivacion() {
        return activacion;
    }

    public void setActivacion(boolean activacion) {
        this.activacion = activacion;
    }
    @OneToOne(fetch=FetchType.LAZY)
    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    @OneToOne(fetch=FetchType.LAZY)
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public boolean isExcepcion() {
        return excepcion;
    }

    public void setExcepcion(boolean excepcion) {
        this.excepcion = excepcion;
    }

    @OneToOne(fetch=FetchType.LAZY)
    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }
}
