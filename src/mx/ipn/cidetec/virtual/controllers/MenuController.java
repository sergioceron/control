package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Materia;
import mx.ipn.cidetec.virtual.entities.Menu;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

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
@Name( "menuController" )
@Scope( ScopeType.CONVERSATION )
public class MenuController {
    private Menu parent = null;
    private Menu menu = new Menu();

    @In
    private SystemController systemController;

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;

	@End
	public void save(){
        if( parent != null ){
            List<Menu> sub = new ArrayList<Menu>();
            sub.add( menu );
            if( parent.isHasSub() ){
                parent.getSubmenu().addAll( sub );
            } else {
                parent.setSubmenu( sub );
            }
            entityManager.persist( parent );
        } else {
            entityManager.persist( menu );
        }
        systemController.reboot();
		entityManager.flush();

        menu = new Menu();
        parent = null;
	}

    public void prepareToRemove(Menu menu){
        this.menu = menu;
    }

    public void remove(){
        entityManager.remove( menu );
        entityManager.flush();

        menu = null;
        parent = null;
    }


    public Menu getMenu() {
        return menu;
    }

    public void setMenu( Menu menu ) {
        this.menu = menu;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent( Menu parent ) {
        this.parent = parent;
    }

    public boolean isRemovable() {
        return true;
    }
}
