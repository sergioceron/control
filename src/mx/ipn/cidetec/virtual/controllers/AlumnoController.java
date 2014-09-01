package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Alumno;
import mx.ipn.cidetec.virtual.entities.Profesor;
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
@Name( "alumnoController" )
@Scope( ScopeType.CONVERSATION )
public class AlumnoController {
	private Alumno alumno = new Alumno();

	@In
	private EntityManager entityManager;

	@End
	public void guardar(){
		entityManager.persist( alumno );
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno( Alumno alumno ) {
		this.alumno = alumno;
	}

	public Alumno.Status[] getStatuses(){
		return Alumno.Status.values();
	}

	public List<Profesor> getProfesores(){
		Query query = entityManager.createQuery( "from Profesor p" );
		return query.getResultList();
	}

	public List<Alumno> getAlumnos(){
		Query query = entityManager.createQuery( "from Alumno a" );
		return query.getResultList();
	}
}
