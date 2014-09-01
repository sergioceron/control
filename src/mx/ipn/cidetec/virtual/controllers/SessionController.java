package mx.ipn.cidetec.virtual.controllers;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

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
	}

	public boolean isCaptcha() {
		return captcha;
	}

	public void setCaptcha( boolean captcha ) {
		this.captcha = captcha;
	}
}
