package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 30/08/14 05:25 PM
 */
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public class Account {

	private Long id;
	private String nombre;
	private String apellidoP;
	private String apellidoM;
	private int edad;
	private int sexo;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	public String getApellidoP() {
		return apellidoP;
	}

	public void setApellidoP( String apellidoP ) {
		this.apellidoP = apellidoP;
	}

	public String getApellidoM() {
		return apellidoM;
	}

	public void setApellidoM( String apellidoM ) {
		this.apellidoM = apellidoM;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad( int edad ) {
		this.edad = edad;
	}

	public int getSexo() {
		return sexo;
	}

	public void setSexo( int sexo ) {
		this.sexo = sexo;
	}

	@Transient
	public String getFullName(){
		return nombre + " " + apellidoP + " " + apellidoM;
	}
}
