package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Role;
import mx.ipn.cidetec.virtual.entities.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;

import javax.persistence.EntityManager;

/**
 * Created by Usuario on 16/09/2014.
 */
@Name("userController")
@Scope(ScopeType.CONVERSATION)
public class UserController {
    private String password = "";
    private User user = new User();

    @In
    private IdentityManager identityManager;

    @In
    private EntityManager entityManager;

    @End
    public String save() {
        new RunAsOperation() {
            public void execute() {
                try {
                    if (!identityManager.userExists(user.getUsername())) {
                        identityManager.createUser(user.getUsername(),
                                user.getHash(), user.getName(), "");
                        for (Role role : user.getRoles())
                            identityManager.grantRole(user.getUsername(), role.getRolename());
                    } else {
                        entityManager.persist(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.addRole("admin").run();

        user = new User();
        return "success";
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
}
