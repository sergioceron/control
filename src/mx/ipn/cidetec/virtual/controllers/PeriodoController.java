package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Materia;
import mx.ipn.cidetec.virtual.entities.Periodo;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by Usuario on 28/09/2014.
 */
@Name("periodoController")
@Scope(ScopeType.CONVERSATION)
public class PeriodoController {
    private Periodo periodo = new Periodo();
    private boolean removable = true;
    private boolean[] usage = {false, false};

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    @In(scope = ScopeType.APPLICATION)
    private SystemController systemController;

    @End
    public String save(){
        entityManager.persist( periodo );
        entityManager.flush();
        systemController.reboot();
        return "success";
    }

    public void prepareToRemove(Periodo periodo){
        this.periodo = periodo;
        usage[0] = periodo.equals(systemController.getPeriodoActual());
        Query query = entityManager.createQuery("from Curso c where c.periodo = :periodo");
        query.setParameter("periodo", periodo);
        usage[1] = query.getResultList().size() > 0;

        removable = !usage[0] && !usage[1];
    }

    public void remove(){
        entityManager.remove( periodo );
        entityManager.flush();
        periodo = new Periodo();
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean[] getUsage() {
        return usage;
    }
}
