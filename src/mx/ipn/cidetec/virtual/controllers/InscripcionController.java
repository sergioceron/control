package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

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
    private Alumno alumno = new Alumno();


    @In(scope = ScopeType.SESSION, required = false)
    private Account account;

    @In
    private EntityManager entityManager;

    @In(scope = ScopeType.APPLICATION)
    private SystemController systemController;

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public void inscribir(Curso curso) {
        if (account != null) {
            if (account instanceof Alumno) {
                Alumno _alumno = entityManager.find(Alumno.class, account.getId());
                if (!hasCurso(curso)) {
                    Calificacion calificacion = new Calificacion();
                    calificacion.setAlumno(_alumno);
                    calificacion.setCurso(curso);
                    calificacion.setPeriodo(systemController.getPeriodoActual());
                    _alumno.getCalificaciones().add(calificacion);
                    entityManager.persist(_alumno);
                } else {
                    for (Calificacion calificacion : _alumno.getCalificaciones()) {
                        if (calificacion.getCurso().equals(curso)) {
                            entityManager.remove(calificacion);
                        }
                    }
                }
            }
        }
    }

    public List<Curso> getCursosDisponibles(){
        Query query = entityManager.createQuery("from Curso c where c.enabled=true");
        return query.getResultList();
    }

    public boolean hasCurso(Curso curso) {
        if (account != null) {
            if (account instanceof Alumno) {
                for (Calificacion calificacion : ((Alumno) account).getCalificaciones()) {
                    if (calificacion.getCurso().equals(curso)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @End
    public String save() {
        entityManager.flush();
        return "success";
    }

}