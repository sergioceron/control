package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Profesor;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.persistence.EntityManager;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name("profesorController")
@Scope(ScopeType.CONVERSATION)
public class ProfesorController {
	private User user = new User();
	private Profesor profesor = new Profesor();

	private boolean account;

	@In
	private EntityManager entityManager;

	@In
	private IdentityManager identityManager;

	@End
	public String save() {

		if( account ) {
			new RunAsOperation() {
				public void execute() {
					try {
						if( !identityManager.userExists( user.getUsername() ) ) {
							identityManager.createUser( user.getUsername(),
									user.getHash(), user.getName(), "" );
						}
					} catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}.addRole( "admin" ).run();
		} else {
			entityManager.persist( profesor );
			entityManager.flush();
		}
		return "success";
	}

	@Observer(JpaIdentityStore.EVENT_PRE_PERSIST_USER)
	public void prePersistUser( User pNewUser ) {
		pNewUser.setAccount( profesor );
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor( Profesor profesor ) {
		this.profesor = profesor;
	}

	public User getUser() {
		return user;
	}

	public void setUser( User user ) {
		this.user = user;
	}

	public boolean isAccount() {
		return account;
	}

	public void setAccount( boolean account ) {
		this.account = account;
	}
}