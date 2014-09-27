package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sergio on 12/06/2014.
 */

@Entity
public class Alumno extends Account {

	private String matricula;
    private String grado;
	private Semestre semestre;
	private Status status = Status.NUEVO;
	private Profesor asesor;
	private Programa programa;
	private List<Calificacion> calificaciones;
    private List<EvaluacionAlumno> evaluaciones;
	private PlanEstudios planEstudios;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula( String matricula ) {
		this.matricula = matricula;
	}

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    @ManyToOne
	public Semestre getSemestre() {
		return semestre;
	}

	public void setSemestre( Semestre semestre ) {
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

	@OneToMany(cascade = CascadeType.ALL)
	public List<Calificacion> getCalificaciones() {
		return calificaciones;
	}

	public void setCalificaciones( List<Calificacion> calificaciones ) {
		this.calificaciones = calificaciones;
	}

    @OneToMany
    public List<EvaluacionAlumno> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<EvaluacionAlumno> evaluaciones) {
        this.evaluaciones = evaluaciones;
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
