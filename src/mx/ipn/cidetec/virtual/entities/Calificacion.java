package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 26/08/14 06:54 PM
 */
@Entity
public class Calificacion {

	private Long id;
	private Alumno alumno;
	private Curso curso;
	private double calificacion;
	private Calificacion recurse;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	@ManyToOne
	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno( Alumno alumno ) {
		this.alumno = alumno;
	}

	@ManyToOne
	public Curso getCurso() {
		return curso;
	}

	public void setCurso( Curso curso ) {
		this.curso = curso;
	}

	public double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion( double calificacion ) {
		this.calificacion = calificacion;
	}

	@OneToOne
	public Calificacion getRecurse() {
		return recurse;
	}

	public void setRecurse( Calificacion parent ) {
		this.recurse = parent;
	}
}
