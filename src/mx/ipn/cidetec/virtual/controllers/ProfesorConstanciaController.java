package mx.ipn.cidetec.virtual.controllers;

/**
 * Created by Usuario on 09/10/2014.
 */

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private List<Curso> cursosMaestria;
    private List<Curso> cursosDoctorado;
    private boolean all = false;

    @In
    private EntityManager entityManager;

	@Logger
	private Log log;

    @Begin(join = true)
    public String prepare() {
        cursosDoctorado = new ArrayList<Curso>();
        cursosMaestria = new ArrayList<Curso>();
        Query query;
        if( programa == null ){
            query = entityManager.createQuery("from Curso c where c.profesor = :profesor and c.periodo = :periodo");
            query.setParameter("profesor", profesor);
            query.setParameter("periodo", periodo);
            all = true;
        } else {
            query = entityManager.createQuery("from Curso c where c.profesor = :profesor and c.periodo = :periodo and c.materia.programa = :programa");
            query.setParameter("profesor", profesor);
            query.setParameter("periodo", periodo);
            query.setParameter( "programa", programa );
            all = false;
        }
        cursos = query.getResultList();

        for( Curso curso : cursos ) {
            if( curso.getMateria().getPrograma().getAbbr().equals( "MTC" ) ){
                cursosMaestria.add( curso );
            } else {
                cursosDoctorado.add( curso );
            }
        }

        return "success";
    }

    public Date getDate(){
        return new Date();
    }

    public String getFecha(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
        return sdf.format(new Date());
    }

    public String getPeriodoFormateado(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        String mesInicio = sdf.format(periodo.getInicio());
        String mesTermino = sdf.format(periodo.getTermino());

        return mesInicio + "-" + mesTermino + " " + periodo.getAnyo();
    }

    public String horarioFormateado(Curso curso){
        List<Hora> horas = curso.getHorario();
	    Collections.sort( horas );

        if( horas.size() == 0 ) return "";

	    Hora primera = horas.get(0);
        Hora segunda = horas.size() > 1 ? horas.get(1) : null;
        String hora = primera.getHoraInicio() + "-" + primera.getHoraFin();
	    String hora0 = "";
        String dia = dias[primera.getDiaSemana()];
        if (segunda != null){
	        if( !segunda.getHoraInicio().equals( primera.getHoraInicio() ) ) {
		        hora0 = hora;
		        hora  = segunda.getHoraInicio() + "-" + segunda.getHoraFin();
	        }
            dia += " " + hora0 + " y " + dias[segunda.getDiaSemana()];
        }
        return dia + " " +  hora;
    }

    public int getTotalHoras(){
        int total = 0;
        for (Curso curso : cursos) {
            Materia.Tipo tipo = curso.getMateria().getTipo();
            total += (tipo.getHorasPractica() + tipo.getHorasTeoria()) * periodo.getSemanas();
        }
        return total;
    }

    public int getTotalHorasMaestria(){
        int total = 0;
        for (Curso curso : cursos) {
            if( curso.getMateria().getPrograma().getAbbr().equals( "MTC" ) ) {
                Materia.Tipo tipo = curso.getMateria().getTipo();
                total += ( tipo.getHorasPractica() + tipo.getHorasTeoria() ) * periodo.getSemanas();
            }
        }
        return total;
    }

    public int getTotalHorasDoctorado(){
        int total = 0;
        for (Curso curso : cursos) {
            if( !curso.getMateria().getPrograma().getAbbr().equals( "MTC" ) ) {
                Materia.Tipo tipo = curso.getMateria().getTipo();
                total += ( tipo.getHorasPractica() + tipo.getHorasTeoria() ) * periodo.getSemanas();
            }
        }
        return total;
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

    public List<Curso> getCursosMaestria() {
        return cursosMaestria;
    }

    public List<Curso> getCursosDoctorado() {
        return cursosDoctorado;
    }

    public boolean isAll() {
        return all;
    }

    public boolean isMaestria() {
        return cursosMaestria.size() > 0;
    }

    public boolean isDoctorado() {
        return cursosDoctorado.size() > 0;
    }
}
