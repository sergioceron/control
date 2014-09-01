package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import javax.persistence.EntityManager;
import javax.ws.rs.*;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 6/08/14 09:00 PM
 */
@Name( "userService" )
@Path( "/user" )
public class UserService {

	@In
	EntityManager entityManager;

	@RequestParameter( "email" )
	String email;

	@GET
	@Path( "/exist" )
	@Produces( "application/json" )
	public boolean validate() {
		return entityManager.find( User.class, email ) ==  null;
	}
}
