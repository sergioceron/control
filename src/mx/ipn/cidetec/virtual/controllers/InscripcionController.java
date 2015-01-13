package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name("inscripcionController")
@Scope(ScopeType.CONVERSATION)
public class InscripcionController {
    private Alumno alumno;
    private Curso curso;

    @In
    private EntityManager entityManager;

    @In
    private SystemController systemController;

    public void inscribir() {
        if (!hasCurso(curso)) {
            Calificacion calificacion = new Calificacion();
            calificacion.setAlumno(alumno);
            calificacion.setCurso(curso);
            alumno.getCalificaciones().add(calificacion);
        } else {
            for (Calificacion calificacion : alumno.getCalificaciones()) {
                if (calificacion.getCurso().equals(curso)) {
                    alumno.getCalificaciones().remove(calificacion);
                }
            }
        }
        entityManager.persist(alumno);
    }

    public List<Curso> getCursosDisponibles(){
        Query query = entityManager.createQuery("from Curso c where c.enabled = true and c.periodo = :periodo");
        query.setParameter("periodo", systemController.getPeriodoActual());
        return query.getResultList();
    }

    public boolean hasCurso(Curso curso) {
        for (Calificacion calificacion : alumno.getCalificaciones()) {
            if (calificacion.getCurso().equals(curso)) {
                return true;
            }
        }
        return false;
    }

    public List<Alumno> getAlumnosInscritos(){
        Query query = entityManager.createQuery("from Alumno a where a.status = :status");
        query.setParameter("status", Alumno.Status.INSCRITO);
        return query.getResultList();
    }

    @End
    public String save() {
        alumno.setSemestre( alumno.getSemestre() + 1 );
        alumno.setStatus( Alumno.Status.INSCRITO );
        entityManager.persist(alumno);
        entityManager.flush();
        return "success";
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = entityManager.find(Alumno.class, alumno.getId());
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}