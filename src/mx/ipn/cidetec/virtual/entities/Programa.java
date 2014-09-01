package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Adrian on 26/06/2014.
 */
@Entity
public class Programa {
    private Long id;
    private String nombre;
	private int semestres;
    private List<PlanEstudios> planes;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public int getSemestres() {
		return semestres;
	}

	public void setSemestres( int semestres ) {
		this.semestres = semestres;
	}

	@OneToMany
    public List<PlanEstudios> getPlanes() {
        return planes;
    }

    public void setPlanes( List<PlanEstudios> planEstudios ) {
        this.planes = planEstudios;
    }
}
