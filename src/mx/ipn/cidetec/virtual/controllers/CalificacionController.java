package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Calificacion;
import mx.ipn.cidetec.virtual.entities.Curso;
import mx.ipn.cidetec.virtual.entities.Hora;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.persistence.EntityManager;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "calificacionController" )
@Scope( ScopeType.CONVERSATION )
public class CalificacionController {
    private Curso curso;
    private Calificacion calificacion;

	@In
	private EntityManager entityManager;

    @In(create = true)
    private InscripcionController inscripcionController;

    public void alta(){
        inscripcionController.inscribir();
        entityManager.flush();
        entityManager.refresh(curso);
    }

    public void baja(){
        curso.getCalificaciones().remove(calificacion);
        entityManager.persist(curso);
        entityManager.flush();
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = entityManager.find(Curso.class, curso.getId());
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }
}
