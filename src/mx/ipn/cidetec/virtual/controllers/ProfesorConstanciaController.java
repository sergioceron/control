package mx.ipn.cidetec.virtual.controllers;

/**
 * Created by Usuario on 09/10/2014.
 */

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Name("profesorConstanciaController")
@Scope( ScopeType.CONVERSATION )
public class ProfesorConstanciaController {
    private final String dias[] = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
    private final String semestres[] = { "", "1er semestre", "2do semestre", "3er semestre", "4to Semestre", "5to Semestre", "6to Semestre", "7mo Semestre", "8vo Semestre" };

    private Profesor profesor;
    private Periodo periodo;
    private Programa programa;
    private String constancia;
    private List<Curso> cursos;

    @In
    private EntityManager entityManager;

    @Begin(join = true)
    public String prepare() {
        Query query = entityManager.createQuery("from Curso c where c.profesor = :profesor and c.periodo = :periodo and c.materia.programa = :programa");
        query.setParameter("profesor", profesor);
        query.setParameter("periodo", periodo);
        query.setParameter("programa", programa);
        cursos = query.getResultList();

        return "success";
    }

    public Date getDate(){
        return new Date();
    }

    public String getPeriodoFormateado(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        String mesInicio = sdf.format(periodo.getInicio());
        String mesTermino = sdf.format(periodo.getTermino());

        return mesInicio + "-" + mesTermino + " " + periodo.getAnyo();
    }

    public String horarioFormateado(Curso curso){
        List<Hora> horas = curso.getHorario();
        Hora primera = horas.get(0);
        Hora segunda = horas.size() > 1 ? horas.get(1) : null;
        String hora = primera.getHoraInicio() + "-" + primera.getHoraFin();
        String dia = dias[primera.getDiaSemana()];
        if (segunda != null){
            dia += " y " + dias[segunda.getDiaSemana()];
        }
        return dia + " " +  hora;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public String[] getSemestres() {
        return semestres;
    }

    public void setConstancia(String constancia) {
        this.constancia = constancia;
    }

    public String getConstancia() {
        return constancia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Curso> getCursos() {
        return cursos;
    }
}
