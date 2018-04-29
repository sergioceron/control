package mx.ipn.cidetec.virtual.controllers;

/**
 * Created by Usuario on 09/10/2014.
 */

import mx.ipn.cidetec.virtual.entities.Alumno;
import mx.ipn.cidetec.virtual.entities.Calificacion;
import mx.ipn.cidetec.virtual.utils.CalificacionComparator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import java.text.SimpleDateFormat;
import java.util.*;

@Name( "alumnoConstanciaController" )
@Scope( ScopeType.CONVERSATION )
public class AlumnoConstanciaController {
    private final String semestres[] = { "", "primer", "segundo", "tercer", "cuarto", "quinto", "sexto", "séptimo", "octavo" };

    private Alumno alumno;
    private String constancia;
    private List<String> constancias = new ArrayList<String>();

    @In
    private SystemController systemController;

    @Logger
    private Log log;


    public void prepare( Alumno alumno ) {
        constancias = new ArrayList<String>();
        if( alumno.getStatus() == Alumno.Status.INSCRITO || alumno.getStatus() == Alumno.Status.BAJA
                || alumno.getStatus() == Alumno.Status.EGRESADO ) {
            if( alumno.getSemestre() > 1 )
                constancias.add( "avance" );
            constancias.add( "sip8" );
            constancias.add( "sip8bis" );
            //if (alumno.getStatus() == )
        }
        this.alumno = alumno;
    }

    public List<Calificacion> noRepeatedCalificaciones( Alumno alumno ) {
        Map<String, Calificacion> nonRepeated = new HashMap<String, Calificacion>();
        for( Calificacion cal : alumno.getCalificaciones() ) {
            String clave = cal.getCurso().getMateria().getClave();
            if( nonRepeated.containsKey( clave ) ) {
                Calificacion set = nonRepeated.get( clave );
                if( set.isReprobada() ) {
                    nonRepeated.put( clave, cal );
                }
            } else {
                nonRepeated.put( clave, cal );
            }
        }
        return new ArrayList<Calificacion>( nonRepeated.values() );
    }

    public String getFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd 'de' MMMM 'de' yyyy" );
        return sdf.format( new Date() );
    }

    public List<Calificacion> getCalificaciones() {
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        for( Calificacion calificacion : noRepeatedCalificaciones( alumno ) ) {
            if( calificacion.isSetted() ) {
                calificaciones.add( calificacion );
            }
        }
        return calificaciones;
    }

    public List<String> getMatricula() {
        List<String> m = new ArrayList<String>();
        for( int i = 0; i < alumno.getMatricula().length(); i++ ) {
            m.add( alumno.getMatricula().charAt( i ) + "" );
        }
        return m;
    }

    public Date getDate() {
        return new Date();
    }

    public List<String> getClaveMateria( String clave ) {
        String claveZeros = String.format( "%7s", clave );
        List<String> m = new ArrayList<String>();
        for( int i = 0; i < claveZeros.length(); i++ ) {
            m.add( claveZeros.charAt( i ) + "" );
        }
        return m;
    }

    public String getPromedioGeneral() {
        float suma = 0f;
        int count = 0;
        for( Calificacion calificacion : noRepeatedCalificaciones( alumno ) ) {
            if( calificacion.isSetted() ) {
                suma += calificacion.getCalificacion();
                count++;
            }
        }
        return String.format( "%.1f", suma / ( float ) count );
    }

    public String getPorcentajeAvance() {
        return String.format( "%.1f", 100 * ( float ) getCreditosCursados() / ( float ) alumno.getPlanEstudios().getCreditos() );
    }

    public int getCreditosCursados() {
        int creditos = 0;
        for( Calificacion calificacion : noRepeatedCalificaciones( alumno ) ) {
            if( calificacion.isSetted() ) {
                creditos += calificacion.getCurso().getMateria().getCreditos();
            }
        }
        return creditos;
    }

    public String getInicioFormateado() {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd 'de' MMMM" );
        return sdf.format( systemController.getPeriodoActual().getInicio() );
    }

    public String getTerminoFormateado() {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd 'de' MMMM" );
        return sdf.format( systemController.getPeriodoActual().getTermino() );
    }

    public String getSemestre() {
        return semestres[alumno.getSemestre()];
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public List<Calificacion> getCalificacionesOrdenadas() {
        List<Calificacion> calificaciones = noRepeatedCalificaciones( alumno );

        Map<Long, List<Calificacion>> hashMap = new HashMap<Long, List<Calificacion>>();
        List<Calificacion> ordered = new LinkedList<Calificacion>( calificaciones );
        Collections.sort( ordered, new CalificacionComparator() );

        return ordered;
    }

    public List<String> getConstancias() {
        return constancias;
    }

    public String getConstancia() {
        return constancia;
    }

    public void setConstancia( String constancia ) {
        this.constancia = constancia;
    }
}
