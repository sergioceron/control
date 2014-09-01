package mx.ipn.cidetec.virtual.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by sergio on 12/06/2014.
 */

@Entity
public class Alumno extends Account {

	private String matricula;
	private String semestre;
	private Status status;
	private Profesor asesor;
	private Programa programa;
	private List<Curso> cursos;
	private List<Calificacion> calificaciones;
	private PlanEstudios planEstudios;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula( String matricula ) {
		this.matricula = matricula;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre( String semestre ) {
		this.semestre = semestre;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus( Status status ) {
		this.status = status;
	}

	@ManyToOne
	public Profesor getAsesor() {
		return asesor;
	}

	public void setAsesor( Profesor asesor ) {
		this.asesor = asesor;
	}

	@ManyToOne
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma( Programa programa ) {
		this.programa = programa;
	}

	@OneToMany
	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos( List<Curso> cursos ) {
		this.cursos = cursos;
	}

	@OneToMany
	public List<Calificacion> getCalificaciones() {
		return calificaciones;
	}

	public void setCalificaciones( List<Calificacion> calificaciones ) {
		this.calificaciones = calificaciones;
	}

	@ManyToOne
	public PlanEstudios getPlanEstudios() {
		return planEstudios;
	}

	public void setPlanEstudios( PlanEstudios planEstudios ) {
		this.planEstudios = planEstudios;
	}

	public enum Status {
		NUEVO( "Nuevo" ),
		INSCRITO( "Inscrito" ),
		BAJA( "Baja" ),
		EGRESADO( "Egresado" );

		private String name;

		Status( String name ) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
