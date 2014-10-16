package mx.ipn.cidetec.virtual.controllers;

/**
 * Created by Usuario on 09/10/2014.
 */

import mx.ipn.cidetec.virtual.entities.Alumno;
import mx.ipn.cidetec.virtual.entities.Calificacion;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Name( "constanciaController" )
@Scope( ScopeType.CONVERSATION )
public class ConstanciaController {
    private final String semestres[] = { "", "primer", "segundo", "tercer", "cuarto", "quinto", "sexto", "séptimo", "octavo" };

    private Alumno alumno;

    @In
    private SystemController systemController;

    public Alumno getAlumno() {
        return alumno;
    }

    public List<Calificacion> getCalificaciones(){
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        for (Calificacion calificacion : alumno.getCalificaciones()) {
            if (calificacion.isSetted()){
                calificaciones.add(calificacion);
            }
        }
        return calificaciones;
    }

    public List<String> getMatricula(){
        List<String> m = new ArrayList<String>();
        for (int i = 0; i < alumno.getMatricula().length(); i++) {
            m.add(alumno.getMatricula().charAt(i) + "");
        }
        return m;
    }

    public Date getDate(){
        return new Date();
    }

    public List<String> getClaveMateria(String clave){
        String claveZeros = String.format("%8s", clave);
        List<String> m = new ArrayList<String>();
        for (int i = 0; i < claveZeros.length(); i++) {
            m.add(claveZeros.charAt(i) + "");
        }
        return m;
    }

    public String getPromedioGeneral(){
        float suma = 0f;
        int count = 0;
        for (Calificacion calificacion : alumno.getCalificaciones()) {
            if (calificacion.isSetted()) {
                suma += calificacion.getCalificacion();
                count++;
            }
        }
        return String.format("%.1f", suma/(float)count);
    }

    public String getPorcentajeAvance(){
        return String.format("%.1f" , 100*(float)getCreditosCursados()/(float)alumno.getPlanEstudios().getCreditos());
    }

    public int getCreditosCursados(){
        int creditos = 0;
        for (Calificacion calificacion : alumno.getCalificaciones()) {
            if (calificacion.isSetted()) {
                creditos += calificacion.getCurso().getMateria().getCreditos();
            }
        }
        return creditos;
    }

    public String getInicioFormateado(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM");
        return sdf.format(systemController.getPeriodoActual().getInicio());
    }

    public String getTerminoFormateado(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM");
        return sdf.format(systemController.getPeriodoActual().getTermino());
    }

    public String getSemestre(){
        return semestres[alumno.getSemestre()];
    }


    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}
