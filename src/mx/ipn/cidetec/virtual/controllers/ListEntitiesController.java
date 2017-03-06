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

    public List<Profesor> getProfesores() {
        Query query = entityManager.createQuery( "from Profesor p" );
        return query.getResultList();
    }

    public List<Alumno> getAlumnos() {
        Query query = entityManager.createQuery( "from Alumno a" );
        return query.getResultList();
    }

    public List<PlanEstudios> getPlanes() {
        Query query = entityManager.createQuery( "from PlanEstudios p" );
        return query.getResultList();
    }

    public List<Programa> getProgramas() {
        Query query = entityManager.createQuery( "from Programa p" );
        return query.getResultList();
    }

    public List<Materia> getMaterias() {
        Query query = entityManager.createQuery( "from Materia m" );
        return query.getResultList();
    }

    public List<Curso> getCursos() {
        Query query = entityManager.createQuery( "from Curso c" );
        return query.getResultList();
    }

    public List<Hora> getHoras() {
        Query query = entityManager.createQuery( "from Hora h" );
        return query.getResultList();
    }

    public List<Colonia> getColonias() {
        Query query = entityManager.createQuery( "from Colonia c" );
        return query.getResultList();
    }

    public List<Colonia> getEstados() {
        Query query = entityManager.createQuery( "from Colonia c group by c.estado" );
        return query.getResultList();
    }

    public List<Colonia> getMunicipios() {
        Query query = entityManager.createQuery( "from Colonia c group by c.municipio" );
        return query.getResultList();
    }

    public List<Role> getRoles() {
        Query query = entityManager.createQuery( "from Role r" );
        return query.getResultList();
    }

    public List<User> getUsers() {
        Query query = entityManager.createQuery( "from User u" );
        return query.getResultList();
    }

    public List<Periodo> getPeriodos() {
        Query query = entityManager.createQuery( "from Periodo p" );
        return query.getResultList();
    }

    public List<Announcement> getAnnouncements() {
        Query query = entityManager.createQuery( "from Announcement a" );
        return query.getResultList();
    }

    public List<Menu> getMenus() {
        Query query = entityManager.createQuery( "from Menu m" );
        return query.getResultList();
    }

    public List<EvaluacionCriterio> getEvaluacionPreguntas7() {
        Query query = entityManager.createQuery( "from EvaluacionCriterio e group by e.id  " );
        return query.getResultList();
    }

    public List<RespuestaCriterio> getRespuestaCriterio() {
        Query query = entityManager.createQuery( "from RespuestaCriterio r order by r.id desc" );
        return query.getResultList();
    }

    public List<EvaluacionDocente> getEvaluacionDocente() {
        Query query = entityManager.createQuery( "from EvaluacionDocente " );
        return query.getResultList();
    }

    public List<EvaluacionRespuesta> getEvaluacionRespuesta() {
        Query query = entityManager.createQuery( "from EvaluacionRespuesta " );
        return query.getResultList();
    }

    public List<EvaluacionPeriodo> getEvaluacionPeriodo() {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo " );
        return query.getResultList();
    }

}
