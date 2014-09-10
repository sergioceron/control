package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by sergio on 12/06/2014.
 */
@Entity
public class Profesor extends Account {

	private String institucion;
	private String plantel;
	private String grado;
	private Date fechaGrado;
	private Tipo tipo;
    private List<Curso> cursos;

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion( String institucion ) {
		this.institucion = institucion;
	}

	public String getPlantel() {
		return plantel;
	}

	public void setPlantel( String plantel ) {
		this.plantel = plantel;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado( String grado ) {
		this.grado = grado;
	}

	public Date getFechaGrado() {
		return fechaGrado;
	}

	public void setFechaGrado( Date fechaGrado ) {
		this.fechaGrado = fechaGrado;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo( Tipo tipo ) {
		this.tipo = tipo;
	}

	@OneToMany
    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

	public enum Tipo {
		COLEGIADO("Colegiado"),
		TITULAR("Titular");

		private String name;

		Tipo( String name ) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName( String name ) {
			this.name = name;
		}
	}
}
