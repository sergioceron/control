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
@Name("alumnoController")
@Scope(ScopeType.CONVERSATION)
public class AlumnoController {
    private User user = new User();
    private Alumno alumno = new Alumno();

    private boolean register = false;
    private boolean persisting = false;
    private boolean removable = true;

    @In
    private IdentityManager identityManager;

    @In
    private SystemController systemController;

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    private List<Colonia> colonias;

    public Alumno.Status[] getStatuses() {
        return Alumno.Status.values();
    }
    public Alumno.Tiempo[] getTiempos() {
        return Alumno.Tiempo.values();
    }

    public void remove(){
        Query query = entityManager.createQuery("from User u where u.account=:account");
        query.setParameter("account", alumno);
        for (Object o : query.getResultList()) {
            User u = (User) o;
            entityManager.remove(u);
        }
        for (EvaluacionAlumno evaluacionAlumno : alumno.getEvaluaciones()) {
            entityManager.remove(evaluacionAlumno);
            log.debug("AlumnoController[method=remove, object=#1]", evaluacionAlumno);
        }
        entityManager.remove( alumno );
        entityManager.flush();
        log.debug("AlumnoController[method=remove, persisted]");
        alumno = null;
    }

    public void prepareToRemove(Alumno alumno){
        this.alumno = alumno;
        Query query = entityManager.createQuery("from Calificacion c where c.alumno=:alumno");
        query.setParameter("alumno", alumno);
        removable = query.getResultList().size() == 0;
    }

    // TODO: fix, if empty password submit -> no generator works. fix, if no boleta entered, bypass wizard and block
    @End
    public String save() {
        if (register && alumno.getId() == null) {
            user.setUsername(alumno.getMatricula());
            new RunAsOperation() {
                public void execute() {
                    try {
                        if (!identityManager.userExists(user.getUsername())) {
                            persisting = true;
                            identityManager.createUser(user.getUsername(),
                                    user.getHash(), user.getName(), "");
                            entityManager.flush();
                            if (user.isEnabled())
                                identityManager.enableUser(user.getUsername());
                            identityManager.grantRole(user.getUsername(), "alumno");
                            entityManager.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.addRole("admin").run();
        } else {
            entityManager.persist(alumno);
            entityManager.flush();
        }
        return "success";
    }

    @Observer(JpaIdentityStore.EVENT_PRE_PERSIST_USER)
    public void prePersistUser(User pNewUser) {
        if (persisting) {
            pNewUser.setAccount(alumno);
            persisting = false;
        }
    }

    public void calcularDireccion() {
        Query query = entityManager.createQuery("from Colonia c where c.cp=:cp");
        query.setParameter("cp", alumno.getDireccion().getCp());
        List<Colonia> colonias = query.getResultList();
        if (colonias.size() > 0) {
            alumno.getDireccion().setEstado(colonias.get(0).getEstado());
            alumno.getDireccion().setMunicipio(colonias.get(0).getMunicipio());
            this.colonias = colonias;
        }
    }

    public List<Calificacion> getCalificacionesActuales(){
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        for (Calificacion calificacion : alumno.getCalificaciones()) {
            if (calificacion.getCurso().getPeriodo().equals(systemController.getPeriodoActual())){
                calificaciones.add(calificacion);
            }
        }
        return calificaciones;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
        if (alumno.getDireccion()==null)
            alumno.setDireccion(new Direccion());
    }

    public List<Colonia> getColonias() {
        return colonias;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRemovable() {
        return removable;
    }
}
