package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Profesor;
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
@Name( "profesorController" )
@Scope( ScopeType.CONVERSATION )
public class ProfesorController {
	private Profesor profesor = new Profesor();

	@In
	private EntityManager entityManager;

	@End
	public String save() {
		entityManager.persist( profesor );
		entityManager.flush();
		return "success";
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor( Profesor profesor ) {
		this.profesor = profesor;
	}
}
