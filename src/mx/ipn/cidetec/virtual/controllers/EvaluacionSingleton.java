package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.management.IdentityManager;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/10/15.
 */

@Name("evaluacionSingleton")
@Scope(ScopeType.PAGE)
public class EvaluacionSingleton {
    @In
    private IdentityManager identityManager;

    @In
    private SystemController systemController;

    @In
    private EntityManager entityManager;

    @Logger
    private Log log;

    private List<EvaluacionCriterio> selector;
    private EvaluacionDocente evaluacionDocente;
    private static EvaluacionSingleton sin = new EvaluacionSingleton();
    private String follow_materia;
    private String follow_profesor;
    private Calificacion calificacion;
    private Alumno alumno;
    private MateriasActivas materiasActivas;
    private boolean activado;
    private final String[] criterios = {"PUNTUALIDAD","DOMINIO DE LA ASIGNATURA Y HÁBILIDAD DIDÁCTICA","PROGRAMA","EVALUACIÓN","ACTITUD","OPINIÓN GENERAL","OBSERVACIONES"};
    private float[] criteriosIndices = {4,16,19,23,29,30};
    private int err;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    private EvaluacionSingleton(){
        evaluacionDocente = new EvaluacionDocente();
        calificacion = new Calificacion();
        materiasActivas = new MateriasActivas();
    }
    public static EvaluacionSingleton getSin(){
        return sin;
    }
    public List<EvaluacionCriterio> getSelector() {
        if(selector != null) {
            for (int i = 0; i < selector.size(); i++) {
                EvaluacionCriterio ca = selector.get(i);
            }
            return selector;
        }
        return  null;
    }

    public void setSelector(List<String> selector) {

        List<EvaluacionCriterio> lista = new ArrayList<EvaluacionCriterio>();
        int contador = 1,contIndices = 0;
        String cri = criterios[contIndices];
        for(int i=0; i < selector.size();i++) {
            EvaluacionCriterio ev = new EvaluacionCriterio();
            ev.setPregunta(selector.get(i));
            ev.setCriterio(cri);
            if (contador == criteriosIndices[contIndices] ) {
                contIndices++;
                ev.setCriterio(criterios[contIndices]);
                if(contIndices > 6)
                    contIndices = 0;
            }
            contador++;
            if(contador > 31)
                contador = 1;
            lista.add(ev);
        }
        this.selector = lista;
    }

    public EvaluacionDocente getEvaluacionDocente() {
        return evaluacionDocente;
    }

    public void setEvaluacionDocente(EvaluacionDocente evaluacionDocente) {
        this.evaluacionDocente = evaluacionDocente;
    }

    public String getFollow_materia() {
        return follow_materia;
    }

    public void setFollow_materia(String follow_materia) {
        this.follow_materia = follow_materia;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }

    public void resetList(){
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getFollow_profesor() {
        return follow_profesor;
    }

    public void setFollow_profesor(String follow_profesor) {
        this.follow_profesor = follow_profesor;
    }

    public MateriasActivas getMateriasActivas() {
        return materiasActivas;
    }
    public void setMateriasActivas(MateriasActivas materiasActivas) {
        this.materiasActivas = materiasActivas;
    }

}
