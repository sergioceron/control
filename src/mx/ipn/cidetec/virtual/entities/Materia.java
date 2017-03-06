package mx.ipn.cidetec.virtual.entities;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.List;

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
    private List<Curso> cursos;
    private String temario;

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

	@ManyToOne(fetch = FetchType.LAZY)
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

    public String getTemario() {
        return temario;
    }

    public void setTemario(String temario) {
        this.temario = temario;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,
            mappedBy = "materia")
    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public enum Tipo {
		OBLIGATORIA("Obligatoria", 2, 2),
		SEMINARIO("Seminario", 2, 0),
		OPTATIVA("Optativa", 2, 2);

		private String name;
        private int horasTeoria;
        private int horasPractica;

		Tipo( String name ) {
			this.name = name;
		}
		Tipo( String name, int horasTeoria, int horasPractica ) {
			this.name = name;
            this.horasTeoria = horasTeoria;
            this.horasPractica = horasPractica;
		}

		public String getName() {
			return name;
		}

        public int getHorasTeoria() {
            return horasTeoria;
        }

        public int getHorasPractica() {
            return horasPractica;
        }
    }

}
