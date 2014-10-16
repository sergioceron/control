package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 07/10/2014.
 */
@Entity
public class Menu {
    private Long id;
    private String label;
    private String action;
    private String icon;
    private List<Menu> submenu = new ArrayList<Menu>();
    private boolean active;
    private String role;

    public Menu() {
    }

    public Menu(String label) {
        this.label = label;
        this.action = "#";
    }

    public Menu(String label, String action) {
        this.label = label;
        this.action = action;
    }

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<Menu> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<Menu> submenu) {
        this.submenu = submenu;
    }

    @Transient
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Transient
    public boolean isHasSub() {
        return submenu.size() > 0;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (action != null ? !action.equals(menu.action) : menu.action != null) return false;
        if (label != null ? !label.equals(menu.label) : menu.label != null) return false;
        if (submenu != null ? !submenu.equals(menu.submenu) : menu.submenu != null) return false;

        return true;
    }
}
