package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Profesor;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	private boolean register = false;
    private boolean persisting = false;

	@In
	private EntityManager entityManager;

	@In
	private IdentityManager identityManager;

	@End
	public String save() {
		if( register && profesor.getId() == null ) {
			new RunAsOperation() {
				public void execute() {
					try {
						if( !identityManager.userExists( user.getUsername() ) ) {
                            persisting = true;
							identityManager.createUser( user.getUsername(),
									user.getHash(), user.getName(), "" );
                            entityManager.flush();
                            if (user.isEnabled())
                                identityManager.enableUser(user.getUsername());
                            identityManager.grantRole(user.getUsername(), "profesor");
                            entityManager.flush();
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

    public void remove(){Query query = entityManager.createQuery("from User u where u.account=:account");
        query.setParameter("account", profesor);
        for (Object o : query.getResultList()) {
            User u = (User) o;
            entityManager.remove(u);
        }
        entityManager.remove( profesor );
        entityManager.flush();
        profesor = null;
    }

	@Observer(JpaIdentityStore.EVENT_PRE_PERSIST_USER)
	public void prePersistUser( User pNewUser ) {
        if( persisting ) {
            pNewUser.setAccount( profesor );
            persisting = false;
        }
	}

    public int getCursosCount() {
        if (profesor == null){
            return 0;
        }else {
            if (profesor.getCursos() == null)
                return 0;
        }

        return profesor.getCursos().size();
    }

	public Profesor.Tipo[] getTipos(){
		return Profesor.Tipo.values();
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

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }
}
