package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergio on 12/06/2014.
 */
@Entity
public class Curso {
	private Long id;
	private Materia materia;
	private Profesor profesor;
	private String lugar;
	private String semestre;
	private int turno;
	private List<Hora> horario;
    private List<Calificacion> calificaciones;
    private boolean enabled = true;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	@ManyToOne
	public Materia getMateria() {
		return materia;
	}

	public void setMateria( Materia materia ) {
		this.materia = materia;
	}

	@ManyToOne
	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor( Profesor profesor ) {
		this.profesor = profesor;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar( String lugar ) {
		this.lugar = lugar;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre( String semestre ) {
		this.semestre = semestre;
	}

	public int getTurno() {
		return turno;
	}

	public void setTurno( int turno ) {
		this.turno = turno;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	public List<Hora> getHorario() {
		return horario;
	}

	public void setHorario( List<Hora> horario ) {
		this.horario = horario;
	}

    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE)
    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Curso curso = (Curso) o;

        if (id != null ? !id.equals(curso.id) : curso.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
