package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Menu;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "navigationController" )
@Scope( ScopeType.SESSION )
public class NavigationController {
    private List<Menu> menuList = new ArrayList<Menu>();
    public  List<SelectItem> respuestas;

    public void setActive(String action){
        for (Menu menu : menuList) {
            for (Menu submenu : menu.getSubmenu()) {
                submenu.setActive(false);
            }
            menu.setActive(false);
        }
        for (Menu menu : menuList) {
            for (Menu submenu : menu.getSubmenu()) {
                if (submenu.getAction().equals(action)) {
                    submenu.setActive(true);
                    menu.setActive(true);
                }
            }
            if (menu.getAction().equals(action)) {
                menu.setActive(true);
            }
        }
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
    public void activateQuestions(){
        respuestas = new ArrayList<SelectItem>();
    }
}
