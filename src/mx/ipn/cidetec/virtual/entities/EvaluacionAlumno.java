package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Usuario on 09/09/2014.
 */
@Entity
public class EvaluacionAlumno {

    private Long id;
    private Alumno alumno;
    private Evaluacion evaluacion;
    private Curso curso;
    private List<EvaluacionRespuesta> respuestas;
    private boolean terminada = false;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
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
    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }


    @ManyToOne
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @OneToMany
    public List<EvaluacionRespuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<EvaluacionRespuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public boolean isTerminada() {
        return terminada;
    }

    public void setTerminada(boolean terminada) {
        this.terminada = terminada;
    }

    @Override
    public String toString() {
        return "EvaluacionAlumno{" +
                "id=" + id +
                ", alumno=" + alumno +
                ", evaluacion=" + evaluacion +
                ", curso=" + curso +
                ", respuestas=" + respuestas +
                ", terminada=" + terminada +
                '}';
    }
}
