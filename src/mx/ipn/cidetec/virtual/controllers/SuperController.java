package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import mx.ipn.cidetec.virtual.entities.Role;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 16/09/2014.
 */
@Name( "superController" )
@Scope( ScopeType.CONVERSATION )
public class SuperController {
    private List<SuperUser> metaUsers = new ArrayList<SuperUser>();

    private Map<String, Account> usersAccounts = new HashMap<String, Account>();
    // FALTA DECLARAR LA CLASE AUXILIAR DEL USUARIO QUE SERVIRA PARA GUARDAR LA CONTRASEÑA EN TEXTO PLANO
    @In
    private IdentityManager identityManager;

    @In
    private EntityManager entityManager;

    @Begin(join = true)
    public String init() {
        Query queryRolAlumno = entityManager.createQuery( "from Role r where r.roleId=18" );
        Role alumno = ( Role ) queryRolAlumno.getSingleResult();
        Query queryRolProfesor = entityManager.createQuery( "from Role r where r.roleId=34" );
        Role profesor = ( Role ) queryRolProfesor.getSingleResult();
        Query queryAccounts = entityManager.createQuery( "from Account a" );
        List<Account> accounts = queryAccounts.getResultList();
        for( Account account : accounts ) {
            SuperUser superUser = new SuperUser();
            User user = new User();
            if( account instanceof Profesor ) {
                user.setUsername( ( ( Profesor ) account ).getClave() );
                user.addRole( profesor );
                if (( ( Profesor ) account ).getClave().equals( "Sin registro" )){
                    continue;
                }
            } else if( account instanceof Alumno ) {
                user.setUsername( ( ( Alumno ) account ).getMatricula() );
                user.addRole( alumno );
            }
            String password = new BigInteger( 60, new SecureRandom() ).toString( 32 );
            user.setHash( password );
            superUser.setAccount( account );
            superUser.setUser( user );
            superUser.setPassword( password );
            metaUsers.add( superUser );
            usersAccounts.put( user.getUsername(), account );
        }
        return "success";
    }

    public void save() {
        new RunAsOperation() {
            public void execute() {
                for( SuperUser superUser : metaUsers ) {
                    User user = superUser.getUser();
                    try {
                        if( ! identityManager.userExists( user.getUsername() ) ) {
                            identityManager.createUser( user.getUsername(),
                                    user.getHash(), "example", "" );
                            /*if( user.isEnabled() )
                                identityManager.enableUser( user.getUsername() );*/
                            for( Role role : user.getRoles() )
                                identityManager.grantRole( user.getUsername(), role.getRolename() );
                        } else {
                            entityManager.persist( user );
                            entityManager.flush();
                        }
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        }.addRole( "admin" ).run();
    }

    @Observer( JpaIdentityStore.EVENT_PRE_PERSIST_USER )
    public void prePersistUser( User pNewUser ) {
        pNewUser.setAccount( usersAccounts.get( pNewUser.getUsername() ) );
    }

    @End
    public String finish(){
        save();
        entityManager.flush();
        return "success";
    }

    public List<SuperUser> getMetaUsers() {
        return metaUsers;
    }

    public void setMetaUsers( List<SuperUser> metaUsers ) {
        this.metaUsers = metaUsers;
    }
}
