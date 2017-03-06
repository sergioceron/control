package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by root on 2/02/16.
 */
@Entity
//@Table(name = "EvaluacionGeneral")
public class EvaluacionGeneral implements Serializable {

    private int id;
    private String semestre_anio;
    private Curso curso;
    private float puntualidad;
    private float dominio_asignatura;
    private float programa_impartido;
    private float evaluacion_alumnos;
    private float actitud;
    private float opinion_general;
    private float evaluacion;
    private int alumnos_evaluacion;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemestre_anio() {
        return semestre_anio;
    }

    public void setSemestre_anio(String semestre_anio) {
        this.semestre_anio = semestre_anio;
    }

    @OneToOne(fetch=FetchType.EAGER)
    public Curso getCurso() {
        return curso;
    }
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public float getPuntualidad() {
        return puntualidad;
    }

    public void setPuntualidad(float puntualidad) {
        this.puntualidad = puntualidad;
    }

    public float getDominio_asignatura() {
        return dominio_asignatura;
    }

    public void setDominio_asignatura(float dominio_asignatura) {
        this.dominio_asignatura = dominio_asignatura;
    }

    public float getPrograma_impartido() {
        return programa_impartido;
    }

    public void setPrograma_impartido(float programa_impartido) {
        this.programa_impartido = programa_impartido;
    }

    public float getEvaluacion_alumnos() {
        return evaluacion_alumnos;
    }

    public void setEvaluacion_alumnos(float evaluacion_alumnos) {
        this.evaluacion_alumnos = evaluacion_alumnos;
    }

    public float getActitud() {
        return actitud;
    }

    public void setActitud(float actitud) {
        this.actitud = actitud;
    }

    public float getOpinion_general() {
        return opinion_general;
    }

    public void setOpinion_general(float opinion_general) {
        this.opinion_general = opinion_general;
    }

    public float getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(float evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getAlumnos_evaluacion() {
        return alumnos_evaluacion;
    }

    public void setAlumnos_evaluacion(int alumnos_evaluacion) {
        this.alumnos_evaluacion = alumnos_evaluacion;
    }
}
