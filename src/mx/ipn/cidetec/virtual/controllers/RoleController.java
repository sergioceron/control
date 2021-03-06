package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Role;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;

/**
 * Created by Usuario on 16/09/2014.
 */
@Name("roleController")
@Scope(ScopeType.CONVERSATION)
public class RoleController {
    private Role role = new Role();

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    @End
    public String save() {
        entityManager.persist(role);
        role = new Role();
        return "success";
    }

    public void remove(){
        entityManager.remove( role );
        entityManager.flush();
        role = null;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
