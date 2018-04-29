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
    private boolean setted = false;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno( Alumno alumno ) {
		this.alumno = alumno;
	}

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
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

    public boolean isSetted() {
        return setted;
    }

    public void setSetted(boolean setted) {
        this.setted = setted;
    }

    @Transient
    public boolean isReprobada(){
		return calificacion < 8;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calificacion that = (Calificacion) o;

        if (Double.compare(that.calificacion, calificacion) != 0) return false;
        if (alumno != null ? !alumno.equals(that.alumno) : that.alumno != null) return false;
        if (curso != null ? !curso.equals(that.curso) : that.curso != null) return false;

        return true;
    }

	@Override
	public String toString() {
		return "Calificacion{" +
				"id=" + id +
				", alumno=" + alumno +
				", curso=" + curso +
				", calificacion=" + calificacion +
				", recurse=" + recurse +
				", setted=" + setted +
				'}';
	}
}
