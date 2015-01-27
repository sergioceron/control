package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Configuration;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

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
@Name( "systemService" )
@Path( "/system" )
public class SystemService {

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;

    @In(scope= ScopeType.APPLICATION)
    private SystemController systemController;

    @RequestParameter("pk")
    private String id;

    @RequestParameter("value")
    private String value;

    @Transactional
	@POST
	@Path( "/set" )
	@Produces( "application/json" )
	public boolean validate() {
        Configuration config = entityManager.find(Configuration.class, id);
        if (config != null) {
            config.setValue(value);
            entityManager.persist(config);
            entityManager.flush();
            systemController.setConfiguration(id, value);
            return true;
        }
        return false;
	}
}
