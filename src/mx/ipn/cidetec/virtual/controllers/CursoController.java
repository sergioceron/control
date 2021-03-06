package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Curso;
import mx.ipn.cidetec.virtual.entities.Hora;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "cursoController" )
@Scope( ScopeType.CONVERSATION )
public class CursoController {
	private Curso curso = new Curso();
    private boolean removable = true;

	@In
	private EntityManager entityManager;

	@Logger
	private Log log;


	@In(scope = ScopeType.APPLICATION)
    private SystemController systemController;

	@End
	public String save(){
        //curso.setPeriodo(systemController.getPeriodoActual());
		entityManager.persist( curso );
		entityManager.flush();
		return "success";
	}

    public List<Hora> getHorarioOrdenado(){
        List<Hora> horario = new ArrayList<Hora>();
        for (int i = 0; i < 6; i++) {
            boolean existDay = false;
            if (curso.getHorario() != null ) {
                for (Hora hora : curso.getHorario()) {
                    if (hora.getDiaSemana() == i) {
                        horario.add(hora);
                        existDay = true;
                        break;
                    }
                }
            }
            if (!existDay){
                Hora empty = new Hora();
                empty.setDiaSemana(i);
                empty.setHoraInicio(null);
                horario.add(empty);
            }
        }
        return horario;
    }

    public void prepareToRemove(Curso curso){
        this.curso = curso;
        Query query = entityManager.createQuery("from Calificacion c where c.curso = :curso");
        query.setParameter("curso", curso);

        removable = query.getResultList().size() == 0;
    }

    public void prepareToCopy(Curso curso){
        Curso copy = new Curso();
        copy.setMateria( curso.getMateria() );
        copy.setProfesor( curso.getProfesor() );
        copy.setPeriodo( curso.getPeriodo() );
        copy.setLugar( curso.getLugar() );
        copy.setTurno( curso.getTurno() );
        copy.setSemestre( curso.getSemestre() );
        copy.setEnabled( curso.isEnabled() );
        copy.setGrupo( curso.getGrupo() );

        this.curso = copy;
    }

    public void remove(){
        entityManager.remove( curso );
        entityManager.flush();
        curso = null;
    }

    public int getCalificacionesCount(){
        if (curso.getCalificaciones() == null) return 0;
        return curso.getCalificaciones().size();
    }

	public Curso getCurso() {
		return curso;
	}

	public void setCurso( Curso curso ) {
		this.curso = curso;
	}

    public boolean isRemovable() {
        return removable;
    }
}
