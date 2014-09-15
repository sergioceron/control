package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Semestre;
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
@Name( "semestreController" )
@Scope( ScopeType.CONVERSATION )
public class SemestreController {
	private Semestre semestre = new Semestre();

	@In
	private EntityManager entityManager;

	@End
	public String save(){
		entityManager.persist( semestre );
		entityManager.flush();
		semestre = new Semestre();
		return "success";
	}

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}
