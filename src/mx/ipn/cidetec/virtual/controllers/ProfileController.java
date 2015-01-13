package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
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
@Name("profileController")
@Scope(ScopeType.CONVERSATION)
public class ProfileController {
    private Account account;
    private String password = "";

    @In
    private IdentityManager identityManager;

    @In
    private EntityManager entityManager;

    private List<Colonia> colonias;

    @End
    public String save() {
        entityManager.persist(account);
        entityManager.flush();
        return "success";
    }

    public void changePassword(){
        if (!password.isEmpty()) {
            new RunAsOperation() {
                public void execute() {
                    try {
                        identityManager.changePassword(account.getUser().getUsername(), password);
                        entityManager.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.addRole("admin").run();
        }
        password = "";
    }

    public void calcularDireccion() {
        Query query = entityManager.createQuery("from Colonia c where c.cp=:cp");
        query.setParameter("cp", account.getDireccion().getCp());
        List<Colonia> colonias = query.getResultList();
        if (colonias.size() > 0) {
            account.getDireccion().setEstado(colonias.get(0).getEstado());
            account.getDireccion().setMunicipio(colonias.get(0).getMunicipio());
            this.colonias = colonias;
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Colonia> getColonias() {
        return colonias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
