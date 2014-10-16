package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Periodo;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;

import javax.persistence.EntityManager;

/**
 * Created by Usuario on 28/09/2014.
 */
@Name("periodoController")
@Scope(ScopeType.CONVERSATION)
public class PeriodoController {
    private Periodo periodo = new Periodo();

    @In
    private EntityManager entityManager;

    @In(scope = ScopeType.APPLICATION)
    private SystemController systemController;

    @End
    public String save(){
        entityManager.persist( periodo );
        entityManager.flush();
        systemController.reboot();
        return "success";
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
}
