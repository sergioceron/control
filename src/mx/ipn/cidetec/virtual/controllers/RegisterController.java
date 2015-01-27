package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Activation;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.digest.DigestUtils;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.persistence.EntityManager;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 25/05/14 07:23 PM
 */
@Name("registerController")
@Scope(ScopeType.PAGE)
public class RegisterController {

	@In(create = true)
	private Renderer renderer;

	@In
	EntityManager entityManager;

	@Logger
	private Log log;

	@In
	private IdentityManager identityManager;

	@In
	Identity identity;


	@In(create = true)
	private FacesMessages facesMessages;

	private Activation activation;

	private User user = new User();

	private String rehash;
	private boolean terms;
	private String returnValue;

	public String register() {
		returnValue = "";

		new RunAsOperation() {
			public void execute() {
				try {
					if( !identityManager.userExists( user.getUsername() ) ) {
						if( user.getHash().equals( rehash ) ) {
							if( identityManager.createUser( user.getUsername(),
									user.getHash(), user.getName(), "" ) )

								returnValue = "success";
						} else {
							facesMessages.add( StatusMessage.Severity.WARN, "Both password must match" );
						}
					} else {
						facesMessages.add( StatusMessage.Severity.WARN, "User already exists" );
					}
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}.addRole( "admin" ).run();

		if( returnValue.isEmpty() ) {
			returnValue = "failed";
		} else {

			facesMessages.add( StatusMessage.Severity.INFO, "Account created succesfuly" );

			identity.getCredentials().setUsername( user.getUsername() );
			identity.getCredentials().setPassword( user.getHash() );

			identity.tryLogin();

		}

		return returnValue;
	}


	private String getActivationCode( String username ) {
		String raw = "TrueRunes".concat( username );
		return DigestUtils.md5Hex( raw );
	}


	public User getUser() {
		return user;
	}

	public void setUser( User user ) {
		this.user = user;
	}

	public String getRehash() {
		return rehash;
	}

	public void setRehash( String rehash ) {
		this.rehash = rehash;
	}

	public boolean isTerms() {
		return terms;
	}

	public void setTerms( boolean terms ) {
		this.terms = terms;
	}

	public Activation getActivation() {
		return activation;
	}

}
