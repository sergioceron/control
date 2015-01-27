package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.PlanEstudios;
import mx.ipn.cidetec.virtual.entities.Programa;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
    private boolean removable = true;

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;

	@End
	public String save(){
		entityManager.persist( plan );
		entityManager.flush();
		return "success";
	}

    public void prepareToRemove(PlanEstudios plan){
        this.plan = plan;
        Query query = entityManager.createQuery("from Alumno a where a.planEstudios = :plan");
        query.setParameter("plan", plan);
        removable = query.getResultList().size() == 0;
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

    public boolean isRemovable() {
        return removable;
    }
}
