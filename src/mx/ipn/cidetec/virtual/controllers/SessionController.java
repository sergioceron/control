package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Account;
import mx.ipn.cidetec.virtual.entities.Alumno;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "sessionController" )
@Scope( ScopeType.SESSION )
public class SessionController {
	private int loginTries = 0;
	private boolean captcha = false;

    @In
    private Identity identity;

    @Out(required = false)
    private Account account;

    @In
    private EntityManager entityManager;

	@Observer("org.jboss.seam.security.loginFailed")
	public void loginFailed(){
		if( loginTries >= 2 ) {
			loginTries = 2;
			captcha = true;
		} else {
			loginTries++;
		}
	}

	@Observer("org.jboss.seam.security.loginSuccessful")
	public void loginSuccessful(){
		loginTries = 0;
		captcha = false;

        String username = identity.getCredentials().getUsername();
        Query query = entityManager.createQuery("from User u where u.username=:username");
        query.setParameter("username", username);
        Object object = query.getSingleResult();
        if( object != null ) {
            User user = (User) object;
            account = user.getAccount();
        } else {
            account = null;
        }
	}

	public boolean isCaptcha() {
		return captcha;
	}

	public void setCaptcha( boolean captcha ) {
		this.captcha = captcha;
	}
}
