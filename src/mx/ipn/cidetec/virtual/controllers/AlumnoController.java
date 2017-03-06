package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 14/06/14 08:17 PM
 */
@Name( "alumnoController" )
@Scope( ScopeType.CONVERSATION )
public class AlumnoController {
    private User user = new User();
    private Alumno alumno = new Alumno();
    public List<SelectItem> respuestas = new ArrayList<SelectItem>();
    private boolean register = false;
    private boolean persisting = false;
    private boolean removable = true;
    private boolean status = false;
    private boolean activacion = false;
    private EvaluacionSingleton evaluacionSingleton;

    @In
    private IdentityManager identityManager;

    @In
    private SystemController systemController;

    @In
    private EntityManager entityManager;

    @Logger
    private Log log;

    private List<Colonia> colonias;

    public Alumno.Status[] getStatuses() {
        return Alumno.Status.values();
    }

    public Alumno.Tiempo[] getTiempos() {
        return Alumno.Tiempo.values();
    }

    public void remove() {
        Query query = entityManager.createQuery( "from User u where u.account=:account" );
        query.setParameter( "account", alumno );
        for( Object o : query.getResultList() ) {
            User u = (User) o;
            entityManager.remove( u );
        }
        for( EvaluacionAlumno evaluacionAlumno : alumno.getEvaluaciones() ) {
            entityManager.remove( evaluacionAlumno );
            log.debug( "AlumnoController[method=remove, object=#1]", evaluacionAlumno );
        }
        entityManager.remove( alumno );
        entityManager.flush();
        log.debug( "AlumnoController[method=remove, persisted]" );
        alumno = null;
    }

    public void prepareToRemove( Alumno alumno ) {
        this.alumno = alumno;
        Query query = entityManager.createQuery( "from Calificacion c where c.alumno=:alumno" );
        query.setParameter( "alumno", alumno );
        removable = query.getResultList().size() == 0;
    }

    // TODO: fix, if empty password submit -> no generator works. fix, if no boleta entered, bypass wizard and block
    @End
    public String save() {
        if( register && alumno.getId() == null ) {
            user.setUsername( alumno.getMatricula() );
            new RunAsOperation() {
                public void execute() {
                    try {
                        if( !identityManager.userExists( user.getUsername() ) ) {
                            persisting = true;
                            identityManager.createUser( user.getUsername(),
                                    user.getHash(), user.getName(), "" );
                            entityManager.flush();
                            if( user.isEnabled() )
                                identityManager.enableUser( user.getUsername() );
                            identityManager.grantRole( user.getUsername(), "alumno" );
                            entityManager.flush();
                        }
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }.addRole( "admin" ).run();
        } else {
            entityManager.persist( alumno );
            entityManager.flush();
        }
        return "success";
    }

    @Observer( JpaIdentityStore.EVENT_PRE_PERSIST_USER )
    public void prePersistUser( User pNewUser ) {
        if( persisting ) {
            pNewUser.setAccount( alumno );
            persisting = false;
        }
    }

    public void calcularDireccion() {
        Query query = entityManager.createQuery( "from Colonia c where c.cp=:cp" );
        query.setParameter( "cp", alumno.getDireccion().getCp() );
        List<Colonia> colonias = query.getResultList();
        if( colonias.size() > 0 ) {
            alumno.getDireccion().setEstado( colonias.get( 0 ).getEstado() );
            alumno.getDireccion().setMunicipio( colonias.get( 0 ).getMunicipio() );
            this.colonias = colonias;
        }
    }

    public Date getFechaActualServidor() {
        return new Date();
    }

    public Date evaluador() throws ParseException {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo " );
        if( query.getResultList().size() > 0 ) {
            return ( (EvaluacionPeriodo) query.getResultList().get( 0 ) ).getPeriodo().getTermino();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
            Date d2 = sdf.parse( String.valueOf( sdf.format( systemController.getPeriodoActual().getTermino() ) ) );
            return d2;
        }
    }

    public List<Calificacion> getCalificacionesActuales() throws ParseException {
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        Query query1 = null;
        if( getFechaActualServidor().after( evaluador() ) )
            query1 = entityManager.createQuery( "select ca from Calificacion ca inner join ca.curso c where ca.alumno.matricula=:matricula and c.periodo.id=:id" ).setParameter( "matricula", alumno.getMatricula() ).setParameter( "id", getPeriodoEvaluacion().getId() );
        else
            query1 = entityManager.createQuery( "select ca from Calificacion ca inner join ca.curso c where ca.alumno.matricula=:matricula and c.periodo.id=:id" ).setParameter( "matricula", alumno.getMatricula() ).setParameter( "id", systemController.getPeriodoActual().getId() );

        if( alumno != null && alumno.getCalificaciones() != null ) {
            return query1.getResultList();
        }
        return calificaciones;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno( Alumno alumno ) {
        this.alumno = alumno;
        if( alumno != null )
            if( alumno.getDireccion() == null )
                alumno.setDireccion( new Direccion() );
    }

    public List<Colonia> getColonias() {
        return colonias;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister( boolean register ) {
        this.register = register;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean isStatus() {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo ep where ep.alumno.matricula=:matricula" ).setParameter( "matricula", alumno.getMatricula() );
        List<EvaluacionPeriodo> listado = query.getResultList();
        for( EvaluacionPeriodo evaluacionPeriodo : listado ) {
            if( evaluacionPeriodo.isActivacion() )
                activacion = true;
        }
        return query.getResultList().size() == 0 ? true : false;
    }


    public void setStatus( boolean status ) {
        this.status = status;
    }

    public boolean isActivacion() {
        return activacion;
    }


    public void setActivacion() throws ParseException {
        List<Calificacion> registro = getCalificacionesActuales();
        int cont = 0;
        Query query = entityManager.createQuery( "from EvaluacionPeriodo ep where ep.alumno.matricula=:matricula" ).setParameter( "matricula", alumno.getMatricula() );
        List<EvaluacionPeriodo> lista = query.getResultList();
        for( EvaluacionPeriodo evaluacionPeriodo : lista ) {
            evaluacionPeriodo.setActivacion( true );
            entityManager.persist( evaluacionPeriodo );
            entityManager.flush();
        }
    }

    public Periodo getPeriodoEvaluacion() {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo" );
        return ( (EvaluacionPeriodo) query.getResultList().get( 0 ) ).getPeriodo();
    }

    public Calificacion getDatosCalificacion() {
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        if( alumno != null ) {
            for( Calificacion calificacion : alumno.getCalificaciones() ) {
                if( calificacion.getCurso().getPeriodo().equals( getPeriodoEvaluacion() ) ) {
                    if( evaluacionSingleton.getSin().getFollow_materia().equals( calificacion.getCurso().getMateria().getNombre() ) )
                        calificaciones.add( calificacion );
                    evaluacionSingleton.getSin().setCalificacion( calificacion );
                }
            }
            if( calificaciones.size() > 0 ) {
                return calificaciones.get( 0 );
            }
        }
        return null;
    }

    public List<EvaluacionPeriodo> getEvaluacionActuales() throws IOException {
        if( alumno != null ) {
            Query query = entityManager.createQuery( "from EvaluacionPeriodo ep where ep.alumno.matricula=:matricula" ).setParameter( "matricula", alumno.getMatricula() );
            return query.getResultList();
        }
        return null;
    }

    public boolean isActivado() {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo " );
        List<EvaluacionPeriodo> evaluacionPeriodos = query.getResultList();
        if( alumno != null )
            for( EvaluacionPeriodo evaluacionPeriodo : evaluacionPeriodos )
                if( evaluacionPeriodo.getAlumno().getMatricula().equals( alumno.getMatricula() ) && evaluacionPeriodo.isActivacion() )
                    return true;
        return false;
    }

    public boolean isExcepcion() {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo ep where ep.alumno.matricula=:excepcion " ).setParameter( "excepcion", alumno.getMatricula() );
        if( query.getResultList().size() > 0 ) {
            EvaluacionPeriodo ep = (EvaluacionPeriodo) query.getResultList().get( 0 );
            return ep.isExcepcion();
        }
        return false;
    }

    public void setExepcion( String matricula ) {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo ep where ep.alumno.matricula=:matricula " ).setParameter( "matricula", matricula );
        List<EvaluacionPeriodo> lista = query.getResultList();
        for( EvaluacionPeriodo evaluacionPeriodo : lista ) {
            if( evaluacionPeriodo.isExcepcion() )
                evaluacionPeriodo.setExcepcion( false );
            else
                evaluacionPeriodo.setExcepcion( true );
            entityManager.flush();
        }

        systemController.reboot();
    }

    public String estadoExcepcion( String matricula ) {
        Query query = entityManager.createQuery( "from EvaluacionPeriodo ep where ep.alumno.matricula=:matricula " ).setParameter( "matricula", matricula );
        EvaluacionPeriodo evaluacionPeriodo = (EvaluacionPeriodo) query.getResultList().get( 0 );
        return evaluacionPeriodo.isExcepcion() ? "Activar" : "Desactivar";
    }
}
