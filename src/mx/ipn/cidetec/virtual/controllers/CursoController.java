package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Curso;
import mx.ipn.cidetec.virtual.entities.Hora;
import mx.ipn.cidetec.virtual.entities.Materia;
import mx.ipn.cidetec.virtual.entities.MateriaCategoria;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

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
@Name( "cursoController" )
@Scope( ScopeType.CONVERSATION )
public class CursoController {
	private Curso curso = new Curso();

	@In
	private EntityManager entityManager;

	@End
	public String save(){
		entityManager.persist( curso );
		entityManager.flush();
		return "success";
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso( Curso curso ) {
		this.curso = curso;
	}

}
