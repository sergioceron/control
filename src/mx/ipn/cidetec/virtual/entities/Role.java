package mx.ipn.cidetec.virtual.entities;

import org.jboss.seam.annotations.security.management.RoleName;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 15/06/14 12:23 PM
 */
@Entity
public class Role {
	private Integer roleId;
	private String rolename;
    private String description;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId( Integer roleId ) {
		this.roleId = roleId;
	}

	@RoleName
	public String getRolename() {
		return rolename;
	}

	public void setRolename( String rolename ) {
		this.rolename = rolename;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
