package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Announcement;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

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

	@In
	private Identity identity;

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

	public List<Announcement> getAnnouncements(){
		Query query = entityManager.createQuery( "from Announcement a" );
		List<Announcement> announcements = (List<Announcement>) query.getResultList();
		List<Announcement> myAnnouncements = new ArrayList<Announcement>();
		for( Announcement announcement : announcements ) {
			for( mx.ipn.cidetec.virtual.entities.Role role : announcement.getRoles() ) {
				if( identity.hasRole( role.getRolename() ) ){
					myAnnouncements.add( announcement );
					break;
				}
			}
		}
		return myAnnouncements;
	}
}
