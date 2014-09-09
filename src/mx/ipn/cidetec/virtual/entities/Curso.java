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
	private List<Hora> horario;

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

	@ManyToMany(fetch = FetchType.EAGER)
	public List<Hora> getHorario() {
		return horario;
	}

	public void setHorario( List<Hora> horario ) {
		this.horario = horario;
	}

}
