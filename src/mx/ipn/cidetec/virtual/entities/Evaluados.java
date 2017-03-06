package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * Created by root on 4/01/16.
 */
@Entity
@Table(name="Evaluados")
public class Evaluados {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Alumno alumno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

}
