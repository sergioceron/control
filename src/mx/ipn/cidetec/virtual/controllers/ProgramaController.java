package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Materia;
import mx.ipn.cidetec.virtual.entities.PlanEstudios;
import mx.ipn.cidetec.virtual.entities.Programa;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.omg.CosNaming._BindingIteratorImplBase;

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
@Name( "programaController" )
@Scope( ScopeType.CONVERSATION )
public class ProgramaController {
	private Programa programa = new Programa();
	private List<Materia> materias;

	@In
	private EntityManager entityManager;

	@End
	public String save(){
		entityManager.persist( programa );
		entityManager.flush();
		return "success";
	}

    public void remove(){
        entityManager.remove( programa );
        entityManager.flush();
        programa = null;
    }

	public void showMaterias(Programa programa){
		Query query = entityManager.createQuery( "from Materia m where m.programa.id=:pid" );
		query.setParameter( "pid", programa.getId() );
		materias = query.getResultList();
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma( Programa programa ) {
		this.programa = programa;
	}

	public List<Materia> getMaterias() {
		return materias;
	}

	public void setMaterias( List<Materia> materias ) {
		this.materias = materias;
	}
}
