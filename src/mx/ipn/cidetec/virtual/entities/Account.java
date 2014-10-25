package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 30/08/14 05:25 PM
 */
@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public class Account {

	private Long id;
	private String nombre;
	private String apellidoP;
	private String apellidoM;
	private String correo;
	private Date fechaNacimiento;
    private Direccion direccion = new Direccion();
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo( String correo ) {
		this.correo = correo;
	}

    @Temporal(TemporalType.DATE)
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Embedded
    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
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

    @Transient
    public int getType(){
        if (this instanceof Profesor)
            return 0;
        return 1;
    }
}
