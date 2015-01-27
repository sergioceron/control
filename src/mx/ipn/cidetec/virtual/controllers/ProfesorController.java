package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.persistence.EntityManager;
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
@Name("profesorController")
@Scope(ScopeType.CONVERSATION)
public class ProfesorController {
	private User user = new User();
	private Profesor profesor = new Profesor();

	private boolean register = false;
    private boolean persisting = false;
    private boolean removable = true;

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;

	@In
	private IdentityManager identityManager;

    @In(scope = ScopeType.APPLICATION)
    private SystemController systemController;

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

    public List<Hora> horarioOrdenado(Curso curso){
        List<Hora> horario = new ArrayList<Hora>();
        if (curso != null) {
            for (int i = 0; i < 6; i++) {
                boolean existDay = false;
                if (curso.getHorario() != null) {
                    for (Hora hora : curso.getHorario()) {
                        if (hora.getDiaSemana() == i) {
                            horario.add(hora);
                            existDay = true;
                            break;
                        }
                    }
                }
                if (!existDay) {
                    Hora empty = new Hora();
                    empty.setDiaSemana(i);
                    empty.setHoraInicio(null);
                    horario.add(empty);
                }
            }
        }
        return horario;
    }

    public void prepareToRemove(Profesor profesor){
        this.profesor = profesor;
        Query query = entityManager.createQuery("from Calificacion c where c.curso.profesor =:profesor");
        query.setParameter("profesor", profesor);
        removable = query.getResultList().size() == 0;
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

    public List<Curso> getCurrentCursos(){
        if (profesor.getClave() != null) {
            Query query = entityManager.createQuery("from Curso c where c.enabled = true and c.periodo = :periodo and c.profesor = :profesor");
            query.setParameter("periodo", systemController.getPeriodoActual());
            query.setParameter("profesor", profesor);
            return query.getResultList();
        }
        return new ArrayList<Curso>();
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

    public boolean isRemovable() {
        return removable;
    }
}
