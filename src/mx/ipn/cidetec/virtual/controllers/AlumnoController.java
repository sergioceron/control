package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "alumnoController" )
@Scope( ScopeType.CONVERSATION )
public class AlumnoController {
    private User user = new User();
	private Alumno alumno = new Alumno();

    private boolean account = false;
    private boolean persisting = false;

    @In
    private Identity identity;

    @In
    private IdentityManager identityManager;

	@In
	private EntityManager entityManager;

    private List<Colonia> colonias;

	@End
	public void guardar(){
		entityManager.persist( alumno );
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno( Alumno alumno ) {
		this.alumno = alumno;
	}

	public Alumno.Status[] getStatuses(){
		return Alumno.Status.values();
	}

    public void inscribir(Curso curso){
        String username = identity.getCredentials().getUsername();
        Query query = entityManager.createQuery("from User u where u.username=:username");
        query.setParameter("username", username);
        Object object = query.getSingleResult();
        if( object != null ) {
            User user = (User) object;
            if (user.getAccount() instanceof Alumno){
                ((Alumno) user.getAccount()).getCursos().add(curso);
            }
            entityManager.persist(entityManager.merge(user.getAccount()));
        }

    }

    @End
    public String save() {
        if( account ) {
            new RunAsOperation() {
                public void execute() {
                    try {
                        if( !identityManager.userExists( user.getUsername() ) ) {
                            persisting = true;
                            identityManager.createUser( user.getUsername(),
                                    user.getHash(), user.getName(), "" );
                        }
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }.addRole( "admin" ).run();
        } else {
            entityManager.persist( alumno );
            entityManager.flush();
        }
        return "success";
    }

    @Observer(JpaIdentityStore.EVENT_PRE_PERSIST_USER)
    public void prePersistUser( User pNewUser ) {
        if( persisting ) {
            pNewUser.setAccount( alumno );
            persisting = false;
        }
    }

    public void calcularDireccion(){
        Query query = entityManager.createQuery( "from Colonia c where c.cp=:cp" );
        query.setParameter("cp", alumno.getDireccion().getCp());
        List<Colonia> colonias = query.getResultList();
        if( colonias.size() > 0 ){
            alumno.getDireccion().setEstado(colonias.get(0).getEstado());
            alumno.getDireccion().setMunicipio(colonias.get(0).getMunicipio());
            this.colonias = colonias;
        }
    }

    public List<Colonia> getColonias() {
        return colonias;
    }

    public boolean isAccount() {
        return account;
    }

    public void setAccount(boolean account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
