package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Configuration;
import mx.ipn.cidetec.virtual.entities.Periodo;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 29/09/2014.
 */
@Name("systemController")
@Scope(ScopeType.APPLICATION)
@Startup
public class SystemController {

    @Out(required = false)
    private Periodo actual = null;

    private List<Configuration> settings;

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    @Create
    public void boot(){
        int year = new Date().getYear()+1900;
        int month = new Date().getMonth();
        Query query = entityManager.createQuery("from Periodo p");
        List<Periodo> periodos = query.getResultList();
        Date current = new Date();
        for (Periodo periodo : periodos) {
            if( current.after( periodo.getInicio() ) && current.before( periodo.getTermino() ) ){
                this.actual = periodo;
            }
        }

        query = entityManager.createQuery("from Configuration s");
        settings = query.getResultList();
    }

    public void reboot(){
        boot();
    }

    public Periodo getPeriodoActual(){
        return actual;
    }

    public void setPeriodoActual(Periodo actual){
        this.actual = actual;
    }

    public String getConfiguration(String name){
        for (Configuration setting : settings) {
            if (setting.getName().equals(name)){
                return setting.getValue();
            }
        }
        return null;
    }

    public void setConfiguration(String name, String value){
        for (Configuration setting : settings) {
            if (setting.getName().equals(name)){
                 setting.setValue(value);
            }
        }
    }

    public List<Configuration> getSettings() {
        return settings;
    }

    public void setSettings(List<Configuration> settings) {
        this.settings = settings;
    }
}
