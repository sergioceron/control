package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Adrian on 26/06/2014.
 */
@Entity
public class PlanEstudios {

    private Long id;
    private String nombre;
	private int creditos;
    private boolean vigente;
	private Programa programa;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos( int creditos ) {
		this.creditos = creditos;
	}

	@ManyToOne
	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma( Programa programa ) {
		this.programa = programa;
	}

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }
}
