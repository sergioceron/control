package mx.ipn.cidetec.virtual.entities;

import com.sun.istack.internal.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by sergio on 12/06/2014.
 */
@Entity
public class Materia {
    private String clave;
    private String nombre;
	private Programa programa;
	private Tipo tipo;
	private MateriaCategoria categoria;
	private int creditos;
	private boolean externa;
    private String descripcion;

	public Materia() {
		tipo = Tipo.OBLIGATORIA;
		creditos = 8;
	}

	@Id
    @NotNull
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String name ) {
        this.nombre = name;
    }

	@ManyToOne
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma( Programa programa ) {
		this.programa = programa;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo( Tipo tipo ) {
		this.tipo = tipo;
	}

	@ManyToOne
	public MateriaCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria( MateriaCategoria tipo ) {
		this.categoria = tipo;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos( int creditos ) {
		this.creditos = creditos;
	}

	public boolean isExterna() {
		return externa;
	}

	public void setExterna( boolean externa ) {
		this.externa = externa;
	}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public enum Tipo {
		OBLIGATORIA("Obligatoria"),
		SEMINARIO("Seminario"),
		OPTATIVA("Optativa");

		private String name;

		Tipo( String name ) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
