package mx.ipn.cidetec.virtual.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 26/08/14 08:04 PM
 */
@Entity
public class MateriaCategoria {
	private Long id;
	private String nombre;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}
}
