package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.PlanEstudios;
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
@Name( "planEstudiosController" )
@Scope( ScopeType.CONVERSATION )
public class PlanEstudiosController {
	private PlanEstudios plan = new PlanEstudios();

	@In
	private EntityManager entityManager;

	@End
	public String save(){
		entityManager.persist( plan );
		entityManager.flush();
		return "success";
	}

    public void remove(){
        entityManager.remove( plan );
        entityManager.flush();
        plan = null;
    }

	public PlanEstudios getPlan() {
		return plan;
	}

	public void setPlan( PlanEstudios plan ) {
		this.plan = plan;
	}
}
