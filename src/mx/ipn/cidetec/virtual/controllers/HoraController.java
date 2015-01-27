package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Curso;
import mx.ipn.cidetec.virtual.entities.Hora;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

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

	@Logger
	private Log log;

	@End
	public String save(){
		entityManager.persist( hora );
		entityManager.flush();
		hora = new Hora();
		return "success";
	}

	public Hora getHora() {
		return hora;
	}

	public void setHora( Hora hora ) {
		this.hora = hora;
	}
}
