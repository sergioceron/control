package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
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
@Name( "listEntitiesController" )
@Scope( ScopeType.PAGE )
public class ListEntitiesController {
	private Alumno alumno = new Alumno();

	@In
	private EntityManager entityManager;

	public List<Profesor> getProfesores(){
		Query query = entityManager.createQuery( "from Profesor p" );
		return query.getResultList();
	}

	public List<Alumno> getAlumnos(){
		Query query = entityManager.createQuery( "from Alumno a" );
		return query.getResultList();
	}

	public List<PlanEstudios> getPlanes(){
		Query query = entityManager.createQuery( "from PlanEstudios p" );
		return query.getResultList();
	}

	public List<Programa> getProgramas(){
		Query query = entityManager.createQuery( "from Programa p" );
		return query.getResultList();
	}

	public List<Materia> getMaterias(){
		Query query = entityManager.createQuery( "from Materia m" );
		return query.getResultList();
	}

	public List<Curso> getCursos(){
		Query query = entityManager.createQuery( "from Curso c" );
		return query.getResultList();
	}
}
