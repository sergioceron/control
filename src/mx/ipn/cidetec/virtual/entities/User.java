package mx.ipn.cidetec.virtual.entities;

import com.sun.istack.internal.NotNull;
import org.jboss.seam.annotations.security.management.*;

import javax.persistence.*;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 25/05/14 07:25 PM
 */
@Entity
public class User {
	private String username;
	private String hash;
	private String name;
	private String lastname;
	private boolean enabled;
	private List<Role> roles;
	private Object account;

	@Id
	@NotNull
	@UserPrincipal
	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	@NotNull
	@UserPassword(hash = "md5")
	public String getHash() {
		return hash;
	}

	public void setHash( String hash ) {
		this.hash = hash;
	}

	@UserFirstName
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	@UserLastName
	public String getLastname() {
		return lastname;
	}

	public void setLastname( String lastname ) {
		this.lastname = lastname;
	}

	@UserEnabled
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	@UserRoles
	@ManyToMany(targetEntity = Role.class)
	@JoinTable(name = "UserRoles",
			joinColumns = @JoinColumn(name = "UserId"),
			inverseJoinColumns = @JoinColumn(name = "RoleId"))
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles( List<Role> roles ) {
		this.roles = roles;
	}

	@Transient
	public Object getAccount() {
		return account;
	}

	public void setAccount( Object account ) {
		this.account = account;
	}
}
