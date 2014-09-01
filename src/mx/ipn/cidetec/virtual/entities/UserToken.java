package mx.ipn.cidetec.virtual.entities;

import org.jboss.seam.annotations.security.TokenUsername;
import org.jboss.seam.annotations.security.TokenValue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 25/07/14 02:12 PM
 */
@Entity
public class UserToken implements Serializable {
	private Integer tokenId;
	private String username;
	private String value;

	@Id
	@GeneratedValue
	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId( Integer tokenId ) {
		this.tokenId = tokenId;
	}

	@TokenUsername
	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	@TokenValue
	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

}
