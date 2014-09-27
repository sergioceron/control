package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Calificacion;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.web.RequestParameter;

import javax.persistence.EntityManager;
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

    @RequestParameter("pk")
    private String id;

    @RequestParameter("value")
    private String calificacion;

    @Transactional
    @POST
    @Path("/set")
    @Produces("application/json")
    public boolean validate() {
        Calificacion c = entityManager.find(Calificacion.class, Long.parseLong(id));
        if (c != null) {
            if (!c.isSetted()) {
                c.setCalificacion(Double.parseDouble(calificacion));
                c.setSetted(true);
                entityManager.persist(c);
                entityManager.flush();
            }
            return true;
        }
        return false;
    }
}