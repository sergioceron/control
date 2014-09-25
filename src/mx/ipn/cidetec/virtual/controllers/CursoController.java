package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Curso;
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

    public void remove(){
        entityManager.remove( curso );
        entityManager.flush();
        curso = null;
    }

    public int getCalificacionesCount(){
        return curso.getCalificaciones().size();
    }

	public Curso getCurso() {
		return curso;
	}

	public void setCurso( Curso curso ) {
		this.curso = curso;
	}

}
