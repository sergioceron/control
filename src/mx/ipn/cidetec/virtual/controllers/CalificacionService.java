package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Calificacion;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 6/08/14 09:00 PM
 */
@Name("calificacionService")
@Path("/calificacion")
public class CalificacionService {

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    @RequestParameter("pk")
    private String id;

    @RequestParameter("value")
    private String value;

    @Transactional
    @POST
    @Path("/set")
    @Produces("application/json")
    public boolean validate() {
        Calificacion c = entityManager.find(Calificacion.class, Long.parseLong(id));
        if (c != null) {
            if (!c.isSetted()) {
                c.setCalificacion(Double.parseDouble(value));
                c.setSetted(true);
                entityManager.persist(c);
                entityManager.flush();
	            log.info("CalificacionService[method=set, object=#1, value=#2]", c, value );
            }
            return true;
        }
        return false;
    }

    @Transactional
    @POST
    @Path("/enable")
    @Produces("application/json")
    public boolean enable() {
        Calificacion c = entityManager.find(Calificacion.class, Long.parseLong(id));
        if (c != null) {
            c.setSetted(value.equals("1") ? true : false);
            entityManager.persist(c);
            entityManager.flush();
            return true;
        }
        return false;
    }

    @Transactional
    @POST
    @Path("/acta")
    @Produces("application/json")
    public boolean acta() {
        Query query = entityManager.createQuery("update Curso c set c.acta=:acta where c.id=:id");
        query.setParameter("acta", value);
        query.setParameter("id", Long.parseLong(id));
        boolean result = query.executeUpdate() > 0;
        entityManager.flush();
        return result;
    }
}