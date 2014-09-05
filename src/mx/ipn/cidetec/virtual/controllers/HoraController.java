package mx.ipn.cidetec.virtual.controllers;

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
@Name( "horaController" )
@Scope( ScopeType.CONVERSATION )
public class HoraController {
	private Hora hora = new Hora();

	@In
	private EntityManager entityManager;

	@End
	public String save(){
		entityManager.persist( hora );
		entityManager.flush();
		return "success";
	}

	public Hora getHora() {
		return hora;
	}

	public void setHora( Hora hora ) {
		this.hora = hora;
	}
}
