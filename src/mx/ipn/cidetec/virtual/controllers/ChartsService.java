package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.Alumno;
import mx.ipn.cidetec.virtual.entities.Curso;
import mx.ipn.cidetec.virtual.entities.Periodo;
import mx.ipn.cidetec.virtual.utils.PeriodoComparator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.*;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 6/08/14 09:00 PM
 */
@Name( "chartsService" )
@Path( "/charts" )
public class ChartsService {

    @In
    private EntityManager entityManager;

    @GET
    @Path( "/alumnos" )
    @Produces( "application/json" )
    public Map<String, Object[][]> alumnosChart() {
        Query alumnosQuery = entityManager.createQuery( "from Alumno a" );
        Query cursosQuery = entityManager.createQuery( "from Curso c" );
        Query periodosQuery = entityManager.createQuery( "from Periodo p" );

        List<Alumno> alumnos = alumnosQuery.getResultList();
        List<Curso> cursos = cursosQuery.getResultList();
        List<Periodo> periodos = new LinkedList<Periodo>( periodosQuery.getResultList() );
        Collections.sort( periodos, new PeriodoComparator() );

        Map<String, Object[][]> chart = new HashMap<String, Object[][]>();

        Object[][] chartAlumnos = new Object[periodos.size()][2];
        Object[][] chartCursos = new Object[periodos.size()][2];
        Object[][] chartPeriodos = new Object[periodos.size()][2];

        for( int i = 0; i < periodos.size(); i++ ) {
            Periodo periodo = periodos.get( i );
            int countAlumnos = 0;
            for( Alumno alumno : alumnos ) {
                if( alumno.getMatricula().startsWith( periodo.toString() ) ) {
                    countAlumnos++;
                }
            }
            int countCursos = 0;
            for( Curso curso : cursos ) {
                if( curso.getPeriodo().toString().equals( periodo.toString() ) ) {
                    countCursos++;
                }
            }
            chartAlumnos[i] = new Object[]{ i, countAlumnos };
            chartCursos[i] = new Object[]{ i, countCursos };
            chartPeriodos[i] = new Object[]{ i, periodo.toString() };
        }

        chart.put( "alumnos", chartAlumnos );
        chart.put( "cursos", chartCursos );
        chart.put( "periodos", chartPeriodos );

        return chart;
    }


    @GET
    @Path( "/alumnosByAge" )
    @Produces( "application/json" )
    public List<DonoutPojo> alumnosByAgeChart() {
        Query alumnosQuery = entityManager.createQuery( "from Alumno a" );

        List<Alumno> alumnos = alumnosQuery.getResultList();

        Map<Integer, Integer> ages = new HashMap<Integer, Integer>();

        for( Alumno alumno : alumnos ) {
            if( alumno.getFechaNacimiento() != null ) {
                int age = 2015 - alumno.getFechaNacimiento().getYear();
                if( ages.containsKey( age ) ) {
                    ages.put( age, ages.get( age ) + 1 );
                } else {
                    ages.put( age, 1 );
                }
            }
        }

        List<DonoutPojo> chart = new ArrayList<DonoutPojo>();
        int color = 0;
        for( Map.Entry<Integer, Integer> ageset : ages.entrySet() ) {
            DonoutPojo age = new DonoutPojo( ageset.getKey() + "", ageset.getValue(), color++ );
            chart.add( age );
        }

        return chart;
    }

    @GET
    @Path( "/alumnosByGender" )
    @Produces( "application/json" )
    public List<DonoutPojo> alumnosByAgeGender() {
        Query alumnosQuery = entityManager.createQuery( "from Alumno a" );

        List<Alumno> alumnos = alumnosQuery.getResultList();

        Map<String, Integer> ages = new HashMap<String, Integer>();

        for( Alumno alumno : alumnos ) {
            String sexo = alumno.getSexo() == 1 ? "Masculino" : "Femenino";
            if( ages.containsKey( sexo ) ) {
                ages.put( sexo, ages.get( sexo ) + 1 );
            } else {
                ages.put( sexo, 1 );
            }
        }

        List<DonoutPojo> chart = new ArrayList<DonoutPojo>();
        int color = 0;
        for( Map.Entry<String, Integer> ageset : ages.entrySet() ) {
            DonoutPojo sex = new DonoutPojo( ageset.getKey() + "", ageset.getValue(), color++ );
            chart.add( sex );
        }

        return chart;
    }

    @GET
    @Path( "/alumnosBySemester" )
    @Produces( "application/json" )
    public List<DonoutPojo> alumnosBySemester() {
        Query alumnosQuery = entityManager.createQuery( "from Alumno a" );

        List<Alumno> alumnos = alumnosQuery.getResultList();

        Map<Integer, Integer> semesters = new HashMap<Integer, Integer>();

        for( Alumno alumno : alumnos ) {
            int semestre = alumno.getSemestre();
            if( semesters.containsKey( semestre ) ) {
                semesters.put( semestre, semesters.get( semestre ) + 1 );
            } else {
                semesters.put( semestre, 1 );
            }
        }

        List<DonoutPojo> chart = new ArrayList<DonoutPojo>();
        int color = 0;
        for( Map.Entry<Integer, Integer> ageset : semesters.entrySet() ) {
            DonoutPojo sex = new DonoutPojo( ageset.getKey() + "", ageset.getValue(), color++ );
            chart.add( sex );
        }

        return chart;
    }

    class DonoutPojo {
        private final String[] COLORS = { "#3887DE", "#D85BB9", "#EC9C62", "#34AE65", "#826C56", "#984E86", "#6BC1BE", "#BCD893" };
        private String label;
        private double data;
        private String color;

        public DonoutPojo( String label, double data, int color ) {
            this.label = label;
            this.data = data;
            this.color = COLORS[color];
        }

        public String getLabel() {
            return label;
        }

        public void setLabel( String label ) {
            this.label = label;
        }

        public double getData() {
            return data;
        }

        public void setData( double data ) {
            this.data = data;
        }

        public String getColor() {
            return color;
        }

        public void setColor( String color ) {
            this.color = color;
        }
    }
}
