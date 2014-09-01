package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Materia;
import mx.ipn.cidetec.virtual.entities.MateriaCategoria;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "materiaController" )
@Scope( ScopeType.CONVERSATION )
public class MateriaController {
	private String claveFilter;

	private Materia materia = new Materia();

	@In
	private EntityManager entityManager;

	@End
	public String save(){
		entityManager.persist( materia );
		entityManager.flush();
		return "success";
	}

	public String getClaveFilter() {
		return claveFilter;
	}

	public void setClaveFilter( String claveFilter ) {
		this.claveFilter = claveFilter;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria( Materia materia ) {
		this.materia = materia;
	}

	public Materia.Tipo[] getTipos(){
		return Materia.Tipo.values();
	}

	public List<MateriaCategoria> getCategorias(){
		Query query = entityManager.createQuery( "from MateriaCategoria mc" );
		return query.getResultList();
	}
}
