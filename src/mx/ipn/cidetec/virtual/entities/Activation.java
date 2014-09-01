package mx.ipn.cidetec.virtual.entities;


import javax.persistence.*;
import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 1/08/14 11:13 PM
 */
@Entity
public class Activation {
	private Long id;
	private String code;
	private Date date;
	private String username;

	public Activation() {
		date = new Date();
	}

	public Activation( String code, String username ) {
		this();
		this.code = code;
		this.username = username;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode( String code ) {
		this.code = code;
	}

	@Temporal( TemporalType.TIMESTAMP )
	public Date getDate() {
		return date;
	}

	public void setDate( Date date ) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}
}
