package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
@Name( "sessionController" )
@Scope( ScopeType.SESSION )
public class SessionController {
	private int loginTries = 0;
	private boolean captcha = false;

    @In
    private Identity identity;

    @Out(required = false)
    private Account account;

    @In(create = true)
    private MenuController menuController;

    @In
    private SystemController systemController;

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
        Query query = entityManager.createQuery("from Account a where a.user.username=:username");
        query.setParameter("username", username);
        List accounts = query.getResultList();
        if( accounts.size() > 0 ) {
            account = (Account) accounts.get(0);
            if (account.getDireccion() == null){
                account.setDireccion(new Direccion());
            }
            entityManager.lock(account, LockModeType.NONE);
        } else {
            account = null;
        }

        query = entityManager.createQuery("from Menu m");
        List<Menu> menuList = new ArrayList<Menu>();
        for (Menu menu : (List<Menu>)query.getResultList()) {
            if (identity.hasRole(menu.getRole())) {
                menuList.add(menu);
                if (account instanceof Alumno){
                    if (menu.getAction().equals("/admin/inscripcion.seam")) {
                        if (!(systemController.getConfiguration("inscripciones").equals("true") &&
                                (((Alumno) account).getStatus() == Alumno.Status.DESCONOCIDO || ((Alumno) account).getStatus() == Alumno.Status.NUEVO))) {
                            menuList.remove(menu);
                        }
                    }
                }
            }
        }
        menuController.setMenuList(menuList);
	}

	public boolean isCaptcha() {
		return captcha;
	}

	public void setCaptcha( boolean captcha ) {
		this.captcha = captcha;
	}

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
