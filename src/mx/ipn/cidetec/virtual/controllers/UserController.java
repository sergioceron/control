package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Programa;
import mx.ipn.cidetec.virtual.entities.Role;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Usuario on 16/09/2014.
 */
@Name("userController")
@Scope(ScopeType.CONVERSATION)
public class UserController {
    private String password = "";
    private User user = new User();
    private boolean removable = true;

    @In
    private IdentityManager identityManager;

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    @End
    public String save() {
        new RunAsOperation() {
            public void execute() {
                try {
                    if (!identityManager.userExists(user.getUsername())) {
                        identityManager.createUser(user.getUsername(),
                                user.getHash(), user.getName(), "");
                        entityManager.flush();
                        if (user.isEnabled())
                            identityManager.enableUser(user.getUsername());
                        for (Role role : user.getRoles())
                            identityManager.grantRole(user.getUsername(), role.getRolename());
                        entityManager.flush();
                    } else {
                        entityManager.persist(user);
                        entityManager.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.addRole("admin").run();

        user = new User();
        return "success";
    }

    public void prepareToRemove(User user){
        this.user = user;
        removable = user.getAccount() == null;
    }

    public void remove(){
        entityManager.remove(user);
        entityManager.flush();
        user = null;
    }

    public void changePassword(){
        if (!password.isEmpty()) {
            new RunAsOperation() {
                public void execute() {
                    try {
                        identityManager.changePassword(user.getUsername(), password);
                        entityManager.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.addRole("admin").run();
        }
        password = "";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean accessRoleUser(String userId){
        Query query = entityManager.createQuery("from User");
        List<User> lista = query.getResultList();
        for(User user : lista)
            for(Role role: user.getRoles())
                if(userId.equals(role.getRolename()) || userId.isEmpty())
                    if(role.getRoleId() == 50 || role.getRoleId() == 20)
                        return true;
        return false;

    }
}
