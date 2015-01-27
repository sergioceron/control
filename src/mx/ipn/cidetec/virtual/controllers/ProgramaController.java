package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Alumno;
import mx.ipn.cidetec.virtual.entities.Materia;
import mx.ipn.cidetec.virtual.entities.PlanEstudios;
import mx.ipn.cidetec.virtual.entities.Programa;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
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
    private boolean[] usage = {false, false, false};
    private boolean removable = true;

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;

	@End
	public String save(){
		entityManager.persist( programa );
		entityManager.flush();
		return "success";
	}

    public void prepareToRemove(Programa programa){
        this.programa = programa;
        Query query = entityManager.createQuery("from Materia m where m.programa = :programa");
        query.setParameter("programa", programa);
        usage[0] = query.getResultList().size() > 0;

        query = entityManager.createQuery("from PlanEstudios p where p.programa = :programa");
        query.setParameter("programa", programa);
        usage[1] = query.getResultList().size() > 0;

        query = entityManager.createQuery("from Alumno a where a.programa = :programa");
        query.setParameter("programa", programa);
        usage[2] = query.getResultList().size() > 0;

        removable = !usage[0] && !usage[1] && !usage[2];
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

    public boolean[] getUsage() {
        return usage;
    }

    public boolean isRemovable() {
        return removable;
    }
}
