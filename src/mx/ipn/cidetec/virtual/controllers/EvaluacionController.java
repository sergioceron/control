package mx.ipn.cidetec.virtual.controllers;

import mx.ipn.cidetec.virtual.entities.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.management.IdentityManager;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//La fecha de "Termino" debe ser menor a la del periodo actual.
@Name( "evaluacionController" )
@Scope(ScopeType.CONVERSATION)
public class EvaluacionController implements Serializable {

    @In
    private EntityManager entityManager;


    @In
    private IdentityManager identityManager;

    @In
    private SystemController systemController;

    @Logger
    private Log log;

    private EvaluacionDocente evaluacionDocente;
    private EvaluacionSingleton evaluacionSingleton;
    private String respuesta;
    private List<String> criterio;
    private List<EvaluacionCriterio> criterio_respuesta;
    private MateriasActivas materiasActivasV;
    private int validaError;
    private boolean status = false;
    private boolean removable = true;
    private String checkOption;
    private UIData DataTable;
    private MateriasActivas materiasActivas;
    private Curso curso = new Curso();
    private Alumno alumno = new Alumno();
    private Periodo periodo = new Periodo();
    private String proceso;

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public int getValidaError() {
        return validaError;
    }

    public void setValidaError(int validaError) {
        this.validaError = validaError;
        evaluacionSingleton.getSin().setErr(validaError);
    }

    public EvaluacionController() {
        evaluacionDocente = new EvaluacionDocente();
        criterio = new ArrayList<String>();
        criterio_respuesta = new ArrayList<EvaluacionCriterio>();

    }

    @End
    public String save() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = sdf.parse(String.valueOf(sdf.format(systemController.getPeriodoActual().getInicio())));
        Date d2 = sdf.parse(String.valueOf(sdf.format(systemController.getPeriodoActual().getTermino())));
        if(evaluacionDocente.getTermino().before(d2) && evaluacionDocente.getInicio().after(d1) ) {
            evaluacionDocente.setActivado(true);
            entityManager.persist(evaluacionDocente);
            entityManager.flush();
            systemController.reboot();
            savePeriodo();

            return "success";
        }
        else if(d1.after(evaluacionDocente.getInicio())) {

            return "failureB";
        }else{

            return "failure";
        }
    }
    public long getIdMateria(String materia){
        Query query = entityManager.createQuery("from Curso where materia.nombre=:materia").setParameter("materia",materia);
        List<Curso> lista = query.getResultList();
        return lista.get(0).getId();
    }
    public Periodo getPeriodo(){
        Query query = entityManager.createQuery("from Periodo p where p.id=:periodo").setParameter("periodo",systemController.getPeriodoActual().getId());
        return (Periodo) query.getResultList().get(0);
    }
    public void deleteMateria() {

        EvaluacionPeriodo evaluacionPeriodo = new EvaluacionPeriodo();

        alumno.setMatricula(EvaluacionSingleton.getSin().getCalificacion().getAlumno().getMatricula());
        curso.setId(getIdMateria(EvaluacionSingleton.getSin().getFollow_materia()));
        evaluacionPeriodo.setAlumno(alumno);
        evaluacionPeriodo.setCurso(curso);
        evaluacionPeriodo = findEvaluacionPeriodo(evaluacionPeriodo);

        entityManager.remove(evaluacionPeriodo);
        entityManager.flush();
        systemController.reboot();
    }

    @End
    public String saveEvaluacionRespuesta() {

        long cont = 1;

        for (EvaluacionCriterio evaluacionCriterio : criterio_respuesta) {

            RespuestaCriterio respuestaCriterio = findRespuestaCriterio(evaluacionCriterio.getPregunta());
            EvaluacionCriterio evaluacionCriterio1 = entityManager.find(EvaluacionCriterio.class, cont);

            EvaluacionRespuesta evaluacionRespuesta = new EvaluacionRespuesta();
            if (cont != 31)
                evaluacionRespuesta.setRespuesta(String.valueOf(respuestaCriterio.getClave()));
            else
                evaluacionRespuesta.setRespuesta(evaluacionCriterio.getPregunta());

            evaluacionRespuesta.setCriterio(evaluacionCriterio1);
            evaluacionRespuesta.setProfesor(EvaluacionSingleton.getSin().getFollow_profesor());
            evaluacionRespuesta.setMateria(EvaluacionSingleton.getSin().getFollow_materia());

            entityManager.persist(evaluacionRespuesta);
            cont++;
        }
        evaluacionSingleton.getSin().resetList();
        deleteMateria();
        if(isStatusEvaluacionPeriodo())
            return "final";
        return "success";
    }

    public void insertarMatricula(String matricula) {
        Evaluados evaluados = new Evaluados();
        evaluados.setAlumno(getAlumno(matricula));
        Evaluados eva = findEvaluados(matricula);
        if (eva == null) {
            entityManager.persist(evaluados);
            entityManager.flush();
        }

    }
    public Curso getCurso(long id){
        Query query = entityManager.createQuery("from Curso where id=:id").setParameter("id",id);
        return (Curso)query.getResultList().get(0);
    }
    public Alumno getAlumno(String matricula){
        Query query = entityManager.createQuery("from Alumno where matricula=:matricula").setParameter("matricula", matricula);
        return (Alumno)query.getResultList().get(0);
    }
    @End
    public String savePeriodo() {
        Query query = entityManager.createQuery("select al.matricula as matricula,ma.nombre as materia from Calificacion ca inner join ca.curso cu inner join ca.alumno al inner join cu.materia ma where cu.periodo.id =:id and ca.curso.id = cu.id and cu.materia.clave = ma.clave and al.id = ca.alumno.id and al.status = 2").setParameter("id", systemController.getPeriodoActual().getId());
        List<Object[]> evaluacionPeriodo = query.getResultList();
        for (int i = 0; i < evaluacionPeriodo.size(); i++) {
            EvaluacionPeriodo ep = new EvaluacionPeriodo();
            ep.setAlumno(getAlumno(evaluacionPeriodo.get(i)[0].toString()));
            ep.setCurso(getCurso(getIdMateria(evaluacionPeriodo.get(i)[1].toString())));
            ep.setPeriodo(getPeriodo());
            entityManager.persist(ep);
            entityManager.flush();
            systemController.reboot();
        }
        return "success";
    }

    public String getStatusPeriodo(){
        switch(getValidaError()){
            case 1:
                return "La fecha de \"TERMINO\" debe ser menor a la del periodo actual.";
            case 2:
                return "La fecha de \"INICIO\" debe ser mayor a la del periodo actual.";
        }
        return "";
    }


    @End
    public String update() {

        Query query = entityManager.createQuery("from Periodo order by id desc limit 1");
        Periodo periodo = (Periodo) query.getResultList().get(0);

        if(evaluacionDocente.getTermino().before(periodo.getTermino())) {
            EvaluacionDocente eva = entityManager.find(EvaluacionDocente.class, EvaluacionSingleton.getSin().getEvaluacionDocente().getId());
            eva.setInicio(evaluacionDocente.getInicio());
            eva.setTermino(evaluacionDocente.getTermino());
            entityManager.flush();
            systemController.reboot();
            return "success";
        }else if(evaluacionDocente.getInicio().after(periodo.getInicio())){
            return "faliure";
        }
        else{
            return "failure";
        }
    }

    public void prepareToRemove(EvaluacionDocente evaluacionDocente) {
        this.evaluacionDocente = evaluacionDocente;
        evaluacionSingleton.getSin().setEvaluacionDocente(evaluacionDocente);
    }

    public String remove() {
        String[] querys = {"EvaluacionPeriodo","EvaluacionAlumno","EvaluacionRespuesta","Evaluados"};
        String delete = "delete from ";
        evaluacionDocente = entityManager.merge(evaluacionSingleton.getSin().getEvaluacionDocente());
        entityManager.remove(evaluacionDocente);
        Query[] query = new Query[querys.length];
        for(int i = 0; i < querys.length; i++){
            query[i] = entityManager.createQuery(delete+querys[i]);
            query[i].executeUpdate();
        }
        entityManager.flush();
        evaluacionDocente = new EvaluacionDocente();
        return "success";
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
        criterio.add(respuesta);
        if (criterio.size() == 31) {
            for (int i = 0; i < criterio.size(); i++) {
                EvaluacionCriterio evaluacionCriterio = new EvaluacionCriterio();
                evaluacionCriterio.setPregunta(criterio.get(i));
                criterio_respuesta.add(evaluacionCriterio);
            }
        }
    }


    public EvaluacionDocente getEvaluacionDocente() {
        return evaluacionDocente;
    }

    public void setEvaluacionDocente(EvaluacionDocente evaluacionDocente) {
        this.evaluacionDocente = evaluacionDocente;
        EvaluacionSingleton.getSin().setEvaluacionDocente(evaluacionDocente);
    }
    public boolean isStatusEvaluacionPeriodo(){
        Query query = entityManager.createQuery("from EvaluacionPeriodo ep where ep.alumno.matricula=:matricula").setParameter("matricula",EvaluacionSingleton.getSin().getCalificacion().getAlumno().getMatricula());
            if (query.getResultList().size() == 0)
                return true;
        return false;

    }
    public boolean isStatus() {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        if (query.getResultList().size() == 0) {
            return false;

        } else if (query.getResultList().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public void setEvaluacionMateria(EvaluacionPeriodo evaluacionPeriodo) {
        //EvaluacionSingleton.getSin().setAlumno(alumno);
        criterio.clear();
        respuesta = "";
        criterio_respuesta.clear();
        insertarMatricula(evaluacionPeriodo.getAlumno().getMatricula());
        evaluacionSingleton.getSin().setFollow_materia(evaluacionPeriodo.getCurso().getMateria().getNombre());
        evaluacionSingleton.getSin().setFollow_profesor(evaluacionPeriodo.getCurso().getProfesor().toString());
    }

    public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
            throws ValidatorException {
        if (((String) arg2).length() == 0 || ((String) arg2) == null) {
            throw new ValidatorException(new FacesMessage("\nSeleccione una opcion"));
        }
    }

    public List<EvaluacionCriterio> getListaEvaluacionCriterio() {
        //return EvaluacionSingleton.getSin().getSelector();
        return criterio_respuesta;
    }

    public Evaluados findEvaluados(String matricula) {
        Query query = entityManager.createQuery("from Evaluados ");
        List<Evaluados> lista = query.getResultList();

        for (Evaluados evaluados : lista)
            if (evaluados.getAlumno().getMatricula().equals(matricula))
                return evaluados;
        return null;
    }

    public RespuestaCriterio findRespuestaCriterio(String escala) {
        Query query = entityManager.createQuery("from RespuestaCriterio ");
        List<RespuestaCriterio> lista = query.getResultList();

        for (RespuestaCriterio respuestaCriterio : lista)
            if (respuestaCriterio.getEscala().equals(escala))
                return respuestaCriterio;
        return null;
    }

    public EvaluacionPeriodo findEvaluacionPeriodo(EvaluacionPeriodo evaluacionPeriodo) {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        List<EvaluacionPeriodo> lista = query.getResultList();
        for (EvaluacionPeriodo evaluacionPeriodo1 : lista)
            if (evaluacionPeriodo1.getAlumno().getMatricula().equals(evaluacionPeriodo.getAlumno().getMatricula()) && evaluacionPeriodo1.getAlumno().getMatricula().equals(evaluacionPeriodo.getAlumno().getMatricula()))
                return evaluacionPeriodo1;
        return null;
    }

    public boolean isFindAlumnoPeriodo(String matricula) {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ep where ep.alumno.matricula = :matricula");
        query.setParameter( "matricula", matricula );
        List<EvaluacionPeriodo> lista = query.getResultList();
        if( lista.size() > 0 ) {
            EvaluacionPeriodo ep = lista.get( 0 );
            return ep.isActivacion();
        }
        return false;
    }

    public boolean isFindAlumnoEvaluacion(String matricula) {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ep where ep.alumno.matricula = :matricula");
        query.setParameter( "matricula", matricula );
        List<EvaluacionPeriodo> lista = query.getResultList();
        return lista.size() > 0;
        /*HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        for (EvaluacionPeriodo evaluacionPeriodo1 : lista)
            if (evaluacionPeriodo1.getAlumno().getMatricula().equals(matricula) && !request.getRequestURL().toString().contains("alumno.calificaciones.seam"))
                return true;
        return false;*/
    }

    public boolean isFindEvaluacionPeriodo(String matricula) {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        List<EvaluacionPeriodo> lista = query.getResultList();
        for (EvaluacionPeriodo evaluacionPeriodo1 : lista)
            if (evaluacionPeriodo1.getAlumno().getMatricula().equals(matricula) && evaluacionPeriodo1.isActivacion())
                return true;
        return false;
    }

    public List<EvaluacionPeriodo> getEvaluacionPeriodos() {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        List<EvaluacionPeriodo> lista = query.getResultList();
        List<EvaluacionPeriodo> listaActualizada = new ArrayList<EvaluacionPeriodo>();
        for (EvaluacionPeriodo evaluacionPeriodo1 : lista)
            if (evaluacionPeriodo1.getAlumno().getMatricula().equals(EvaluacionSingleton.getSin().getCalificacion().getAlumno().getMatricula()))
                listaActualizada.add(evaluacionPeriodo1);
        return listaActualizada;
    }

    public String getCheckOption() {
        return checkOption;
    }

    public void setCheckOption(String checkOption) {
        this.checkOption = checkOption;
    }

    public UIData getDataTable() {
        return DataTable;
    }

    public void setDataTable(UIData dataTable) {
        DataTable = dataTable;
    }



    public List<EvaluacionDocente> getEvaluacionDocenteA() {
        Query query = entityManager.createQuery("from EvaluacionDocente ");
        return query.getResultList();
    }

    public List<Evaluados> getEvaluacionPeriodoFinalizada() {
        Query query = entityManager.createQuery("from Evaluados");
        List<Evaluados> evaluados = query.getResultList();
        Query query1 = entityManager.createQuery("from EvaluacionPeriodo ");
        List<EvaluacionPeriodo> evaluacionPeriodo = query1.getResultList();
        List<Evaluados> lista = new ArrayList<Evaluados>();

        if (evaluados.size() != 0)
            for (EvaluacionPeriodo ep : evaluacionPeriodo)
                for (Evaluados eva : evaluados)
                    if (!ep.getAlumno().getMatricula().equals(eva.getAlumno().getMatricula()))
                        if (!existMatricula(lista, eva.getAlumno().getMatricula()) && !isFindEvaluacionPeriodo(eva.getAlumno().getMatricula()))
                            lista.add(eva);
        return lista;

    }

    public boolean existMatricula(List<Evaluados> evaluados, String matricula) {
        for (Evaluados evaluados1 : evaluados)
            if (evaluados1.getAlumno().getMatricula().equals(matricula))
                return true;
        return false;
    }

    public boolean existMatricula1(List<EvaluacionPeriodo> evaluacion, String matricula) {
        for (EvaluacionPeriodo evaluacionPeriodo : evaluacion)
            if (evaluacionPeriodo.getAlumno().getMatricula().equals(matricula))
                return true;
        return false;
    }

    public List<Evaluados> getEvaluacionPeriodoInconclusa() {

        Query query = entityManager.createQuery("from Evaluados");
        List<Evaluados> evaluados = query.getResultList();
        Query query1 = entityManager.createQuery("from EvaluacionPeriodo ");
        List<EvaluacionPeriodo> evaluacionPeriodo = query1.getResultList();

        List<Evaluados> lista = new ArrayList<Evaluados>();
        if (evaluados.size() != 0)
            for (EvaluacionPeriodo ep : evaluacionPeriodo)
                for (Evaluados eva : evaluados)
                    if (ep.getAlumno().getMatricula().equals(eva.getAlumno().getMatricula()) && !lista.contains(eva.getAlumno().getMatricula()))
                        if (!existMatricula(lista, ep.getAlumno().getMatricula()))
                            lista.add(eva);
        return lista;

    }

    public List<EvaluacionPeriodo> getEvaluacionPeriodoRestante() {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        List<EvaluacionPeriodo> evaluacionPeriodo = query.getResultList();
        List<EvaluacionPeriodo> lista = new ArrayList<EvaluacionPeriodo>();
        for (EvaluacionPeriodo evaluacionPeriodo1 : evaluacionPeriodo)
            if (!existMatricula1(lista, evaluacionPeriodo1.getAlumno().getMatricula()))
                lista.add(evaluacionPeriodo1);
        return lista;
    }

    public List<EvaluacionRespuesta> getEvaluacionRespuestaDocente() {
        List<EvaluacionRespuesta> evaluacionRespuestas = entityManager.createNamedQuery("EvaluacionRespuesta.list", EvaluacionRespuesta.class).getResultList();
        return evaluacionRespuestas;
    }

    public int evaluacionRealizadas(EvaluacionRespuesta evaluacionRespuesta) {

        Query query = entityManager.createQuery("from EvaluacionRespuesta  where profesor=:profesor").setParameter("profesor", evaluacionRespuesta.getProfesor());
        List<EvaluacionRespuesta> evaluacionRespuestas = query.getResultList();
        return evaluacionRespuestas.size() / 31;
    }

    public int evaluacionRealizadas(MateriasActivas materiasActivas) {
        this.materiasActivasV = materiasActivas;
        Query query = entityManager.createQuery("from EvaluacionRespuesta ");
        List<EvaluacionRespuesta> evaluacionRespuestas = query.getResultList();
        List<EvaluacionRespuesta> evaluacionRespuestas1 = new ArrayList<EvaluacionRespuesta>();

        for (EvaluacionRespuesta evaluacionRespuesta : evaluacionRespuestas)
            if (materiasActivas.toString().trim().equals(evaluacionRespuesta.getProfesor().trim()) && evaluacionRespuesta.getMateria().trim().equals(materiasActivas.getNombreMateria().trim()))
                evaluacionRespuestas1.add(evaluacionRespuesta);
        return evaluacionRespuestas1.size() / 31;
    }

    public List<MateriasActivas> getMateriasImpartidas() {
        Query query = entityManager.createQuery(" select acc.id,m.nombre as nombreMateria, acc.nombre,acc.apellidoP,acc.apellidoM from Materia m inner join m.cursos c inner join c.profesor acc where c.periodo =:id  and m.clave = c.materia and acc.id = c.profesor.id group by acc.id ").setParameter("id",systemController.getPeriodoActual());
        List<MateriasActivas> materiasActivas1 = new ArrayList<MateriasActivas>();
        List<Object[]> materiasActivas = query.getResultList();

        for (int i = 0; i < materiasActivas.size(); i++) {
            MateriasActivas ma = new MateriasActivas();
            ma.setId(Long.parseLong(materiasActivas.get(i)[0].toString()));
            ma.setNombreMateria(materiasActivas.get(i)[1].toString());
            ma.setNombre(materiasActivas.get(i)[2].toString());
            ma.setApellidoP(materiasActivas.get(i)[3].toString());
            ma.setApellidoM(materiasActivas.get(i)[4].toString());
            materiasActivas1.add(ma);
        }
        return materiasActivas1;
    }

    public void setMateriasActivas(MateriasActivas materiasActivas) {
        this.materiasActivas = materiasActivas;
        evaluacionSingleton.getSin().setMateriasActivas(materiasActivas);
    }

    public boolean isActivado() {
        return evaluacionSingleton.getSin().isActivado();
    }

    public void setActivado(boolean b) {
        evaluacionSingleton.getSin().setActivado(b);
    }

    public List<MateriasActivas> getMateriasDocente() {
        Query query = entityManager.createQuery(" select acc.id,c.id, m.nombre as nombreMateria, acc.nombre,acc.apellidoP,acc.apellidoM, c.semestre,c.grupo,p.nombre as nombrePrograma from Materia m inner join m.cursos c inner join m.programa p  inner join c.profesor acc where c.periodo =:id  and m.clave = c.materia and acc.id = c.profesor.id and m.programa.id = p.id ").setParameter("id",systemController.getPeriodoActual());
        List<MateriasActivas> materiasActivas1 = new ArrayList<MateriasActivas>();
        List<Object[]> materiasActivas = query.getResultList();

        for (int i = 0; i < materiasActivas.size(); i++) {
            if (evaluacionSingleton.getSin().getMateriasActivas().getId() == Long.parseLong(materiasActivas.get(i)[0].toString())) {
                MateriasActivas ma = new MateriasActivas();
                ma.setId(Long.parseLong(materiasActivas.get(i)[0].toString()));
                ma.setId_materia(Long.parseLong(materiasActivas.get(i)[1].toString()));
                ma.setNombreMateria(materiasActivas.get(i)[2].toString());
                ma.setNombre(materiasActivas.get(i)[3].toString());
                ma.setApellidoP(materiasActivas.get(i)[4].toString());
                ma.setApellidoM(materiasActivas.get(i)[5].toString());
                ma.setSemestre(materiasActivas.get(i)[6].toString());
                ma.setGrupo(materiasActivas.get(i)[7].toString());
                ma.setNombrePrograma(materiasActivas.get(i)[8].toString());
                materiasActivas1.add(ma);
            }
        }
        return materiasActivas1;
    }

    public String estadoEvaluacionAlumno(String matricula) {
        if(isFindAlumnoPeriodo(matricula)) {
            proceso = "Continua evaluación";
            return "Termina la(s) evaluaciones pendientes que te corresponden.";
        }
        else{
            proceso = "Inicia evaluación";
            return "Inicia la evaluación correspondiente para poder visualizar tus califiaciones.";
        }
    }


    public int getEvaluacionesAlumnosRestantes(MateriasActivas materiasActivas) {
        this.materiasActivasV = materiasActivas;
        //select al.matricula,acc.nombre,acc.apellidoP,acc.apellidoM,ca.curso_id,ma.nombre from calificacion ca  inner join curso cu inner join account acc on(acc.id = ca.alumno_id) inner join materia ma on (ma.clave = cu.materia_clave) inner join alumno al on(al.id = acc.id)  where ca.curso_id  = 358 and cu.id = 358 ;
        //Query query= entityManager.createQuery(" select  ca.curso from Calificacion ca inner join ca.curso cu inner join ca.alumno al inner join cu.materia ma  where ma.clave = cu.materia.clave and ca.curso.id =:id_materia and cu.id = ca.curso.id and cu.periodo =:periodo ").setParameter("id_materia", materiasActivas.getId_materia()).setParameter("periodo",systemController.getPeriodoActual());
        Query query = entityManager.createQuery(" select  ca.curso from Calificacion ca inner join ca.curso cu inner join ca.alumno al inner join cu.materia ma  where ma.clave = cu.materia.clave and ca.curso.id =:id_materia and cu.id = ca.curso.id and cu.periodo  =:id ").setParameter("id_materia", materiasActivas.getId_materia()).setParameter("id",systemController.getPeriodoActual());
        List<Long> lista = query.getResultList();
        return lista.size();
    }

    public int getEvaluacionesAlumnosTotales(MateriasActivas materiasActivas) {
        this.materiasActivasV = materiasActivas;
        //Query query= entityManager.createQuery(" select  ca.curso from Calificacion ca inner join ca.curso cu inner join ca.alumno al inner join cu.materia ma  where ma.clave = cu.materia.clave and ca.curso.id =:id_materia and cu.id = ca.curso.id and cu.periodo =:periodo ").setParameter("id_materia", materiasActivas.getId_materia()).setParameter("periodo",systemController.getPeriodoActual());
        Query query = entityManager.createQuery(" select  ca.curso from Calificacion ca inner join ca.curso cu inner join ca.alumno al inner join cu.materia ma  where ma.clave = cu.materia.clave and ca.curso.id =:id_materia and cu.id = ca.curso.id and cu.periodo =:id ").setParameter("id_materia", materiasActivas.getId_materia()).setParameter("id",systemController.getPeriodoActual());
        List<Long> lista = query.getResultList();
        return lista.size();
    }
    public boolean emptying() {
        int rest = getEvaluacionesAlumnosRestantes(evaluacionSingleton.getSin().getMateriasActivas()) - evaluacionRealizadas(evaluacionSingleton.getSin().getMateriasActivas());
        return rest == 0 ? true : false;
    }

    public boolean errings(){
        return evaluacionRealizadas(this.materiasActivasV) == 0 ? true : false;
    }

    public String insuficientes(){
        return errings() ? " Sin datos necesarios. " : " ¿Estás seguro de vaciar lo datos ?";
    }
    public void prepare(MateriasActivas materiasActivas) {
        this.materiasActivasV = materiasActivas;
        this.materiasActivas = materiasActivas;
        evaluacionSingleton.getSin().setMateriasActivas(materiasActivas);
    }


    public String statusEvaluacion(MateriasActivas materiasActivas){
        if(evaluacionRealizadas(materiasActivas) == 0)
            return "Insuficiente";
        else if((getEvaluacionesAlumnosRestantes(materiasActivas) - evaluacionRealizadas(materiasActivas)) == 0 )
            return "Completo";
        else
            return "Incompleto";
    }
    public String statusEvaluacionCampo(MateriasActivas materiasActivas){
        if(evaluacionRealizadas(materiasActivas) == 0)
            return "danger";
        else if((getEvaluacionesAlumnosRestantes(materiasActivas) - evaluacionRealizadas(materiasActivas)) == 0 )
            return "success";
        else
            return "warning";
    }

    public MateriasActivas getMateriasActivas() {
        return materiasActivas;
    }

    public MateriasActivas getMateriasActivasV() {
        return materiasActivasV;
    }

    public void setMateriasActivasV(MateriasActivas materiasActivasV) {
        this.materiasActivasV = materiasActivasV;
    }



}

