package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Curso;
import mx.ipn.cidetec.virtual.entities.Hora;
import mx.ipn.cidetec.virtual.entities.Periodo;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "dashboardController" )
public class DashboardController {

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;

    @In(scope = ScopeType.APPLICATION)
    private SystemController systemController;


    public long getAlumnosCount(){
        Query query = entityManager.createQuery("select count(*) from Alumno");
        return (Long)query.getSingleResult();
    }
	public long getProfesoresCount(){
		Query query = entityManager.createQuery("select count(*) from Profesor ");
		return (Long)query.getSingleResult();
	}
	public long getCursosCount(){
		Query query = entityManager.createQuery("select count(*) from Curso ");
		return (Long)query.getSingleResult();
	}
	public long getProgramasCount(){
		Query query = entityManager.createQuery("select count(*) from Programa");
		return (Long)query.getSingleResult();
	}
}
