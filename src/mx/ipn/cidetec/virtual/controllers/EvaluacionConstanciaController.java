package mx.ipn.cidetec.virtual.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import mx.ipn.cidetec.virtual.entities.*;
import org.apache.commons.io.IOUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.management.IdentityManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




/**
 * Created by root on 17/01/16.
 */
@Name("evaluacionConstanciaController")
@Scope(ScopeType.CONVERSATION)
public class EvaluacionConstanciaController implements Serializable{

    @In
    private EntityManager entityManager;
    @In
    private IdentityManager identityManager;
    @In
    private SystemController systemController;

    @Logger
    private Log log;

    private String programaConstancia;
    private String periodoInicio;
    private String periodoFin;
    private String periodoAnio;
    private Curso c = new Curso();
    private Materia m = new Materia();

    private String asignatura;
    private String profesorConstancia;
    private final String instituto = "INSTITUTO POLITÉCNICO NACIONAL";
    private final String centro = "CENTRO DE INNOVACIÓN Y DESARROLLO TECNOLÓGICO DE CÓMPUTO";
    private Profesor profesor;
    private EvaluacionSingleton evaluacionSingleton;
    private MateriasActivas materiasActivas;
    private boolean opciones = false;
    private int tamGeneral = 0;
    private final String[] escala = {"MUY BIEN", "SIEMPRE", "BIEN", "CASI SIEMPRE", "REGULAR", "FRECUENTEMENTE", "MAL", "RARA VEZ", "MUY MAL", "NUNCA"};
    private final String[] criterios = {"I PUNTUALIDAD", "II DOMINIO DE LA ASIGNATURA Y HÁBILIDAD DIDÁCTICA", "III PROGRAMA", "IV EVALUACIÓN", "V ACTITUD", "VI OPINIÓN GENERAL"};
    private final String[] preguntas = {"Asiste a clases", "Es puntual", "Cumple con el horario programado para la clase", "Cumple con el horario de atención a los alumnos",
            "Domina los contenido", "Expone con claridad los temas del curso", "Actualidad de los temas desarrollados", "Utiliza variedad de estrategia de aprendizaje", "Integración con los estudiantes en el aprendizaje", "Utiliza ejemplos en la exposición de temas", "Utiliza y promueve el uso de recursos tecnológicos", "Empleo material didáctico adecuado", "Organiza los temas con una secuencia adecuada", "Resuelve dudas de los temas de las asignaturas", "Estimula la participación de los alumnos", "Promueve la consulta de fuentes bibliográficas",
            "Presentación de contenido de la asignatura", "Presentación de temas acorde al programa", "Cumplimiento del programa",
            "Planteamiento forma de evaluación", "Evaluación de acuerdo al contenido temático", "Grado de dificultad de evaluación de acorde al contenido", "Comenta oportunamente resultados de evaluaciones",
            "Respeto hacia los alumnos", "Consideración opciones alumnos", "Fomento del trabajo colaborativo", "Apoyo y asesoria extraclase", "Respeto a los participantes del curso", "Comportamiento ético y valores politécnicos",
            "Recomendación curso del profesor"};
    private int pos = 7;
    private final int indices[] = {35, 119, 140, 168, 210, 217};
    private final int indicesA[] = {35, 91, 28, 35, 49, 14};
    private final float dimensiones[] = {190, 20, 20, 20, 20, 20, 50};
    private final float dimensionCriterio[] = {10, 40, 75};
    private final int[] saltos = {1, 7, 21, 26, 32, 40};
    private List<EvaluacionRespuesta> lista;
    private final float[] registro = new float[30];
    private final int[][] calificaciones = new int[30][5];
    private int con = 0;
    private int encuestas = 0;
    private float suma = 0;
    private float calificacionFinal = 0;
    private int entradas = 0;
    private final float[] criteriosIndices = {4, 16, 19, 23, 29, 30};
    private final float[] saltosIndices = {4, 12, 3, 4, 6, 1};
    private final String[] header = {"Semestre\nAño","Asignatura","Grupo","Puntualidad","Dominio\nAsignatura","Programa\nImpartido","Evaluación\nde\nAlumnos","Actitud","Opción\nGeneral","Evaluación","No. Alumnos\nque\nEvaluaron"};
    public PdfWriter writer;
    private final float[] dimensionHead = new float[]{ 33,49,30,20,20,20,25,20,20,20,25 };



    public EvaluacionConstanciaController() {

    }

    public boolean getOpciones() {
        return opciones;
    }

    public void setOpciones(boolean opciones) {
        this.opciones = opciones;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public String getFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
        return sdf.format(new Date());
    }

    public int[][] zeros(int[][] calificaciones) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 5; j++) {
                calificaciones[i][j] = 0;
            }
        }
        return calificaciones;
    }

    public void generedReports() throws IOException, WriteException, DocumentException, ParseException {
        getEvaluacionRespuesta();

            if (opciones)
                genetedXLS();
             else
                generatedPDF();


    }

    public void getEvaluacionRespuesta() throws ParseException {
        this.materiasActivas = evaluacionSingleton.getSin().getMateriasActivas();
        programaConstancia = this.materiasActivas.getNombrePrograma().toUpperCase();
        periodoInicio = String.valueOf(new SimpleDateFormat("MMMMM").format(periodoInicio())).toUpperCase();
        periodoFin = String.valueOf(new SimpleDateFormat("MMMMM").format(periodoTermino())).toUpperCase();
        periodoAnio = String.valueOf(systemController.getPeriodoActual().getAnyo());
        asignatura = this.materiasActivas.getNombreMateria().toUpperCase();
        profesorConstancia = this.materiasActivas.toString().toUpperCase();
        Query query = entityManager.createQuery("from EvaluacionRespuesta where profesor=:profesor").setParameter("profesor", this.materiasActivas.toString());
        lista = query.getResultList();
        con = 0;
        encuestas = lista.size() / 31;
        suma = 0;
        calificacionFinal = 0;
        entradas = 0;
        zeros(calificaciones);
        for (EvaluacionRespuesta evaluacionRespuesta : lista) {
            if ((con + 1) != 31)
                calificaciones[con][Integer.parseInt(evaluacionRespuesta.getRespuesta()) - 1] += 1;
            if ((con + 1) == evaluacionRespuesta.getCriterio().getId() && evaluacionRespuesta.getCriterio().getId() != 31) {
                registro[con] += Float.parseFloat(evaluacionRespuesta.getRespuesta());
            }
            con++;
            if (con > registro.length)
                con = 0;
        }
    }

    public void genetedXLS() throws WriteException, IOException {

        calificacionFinal = 0;

        File archivo = new File("Ev_" + this.materiasActivas.toString() + "_" + this.materiasActivas.getNombreMateria() + "_" + this.materiasActivas.getSemestre() + ".xls");
        WritableWorkbook workbook =
                Workbook.createWorkbook(archivo);
        WritableSheet sheet =
                workbook.createSheet("Ev_" + this.materiasActivas.toString() + "_" + this.materiasActivas.getNombreMateria() + "_" + this.materiasActivas.getSemestre(), 0);
        int pos = 1;
        for (int j = 0; j < criterios.length; j++) {
            sheet.addCell(new Label(1, j + saltos[j]+11, criterios[j], new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
            if (j + 1 < criterios.length) {
                for (int l = saltos[j]; l < saltos[j + 1] - 2; l++) {
                    sheet.addCell(new Label(1, l + j + 12, pos + " - " + preguntas[pos - 1]));
                    int n;
                    for (n = 0; n < 5; n++)
                        sheet.addCell(new jxl.write.Number(n + 7, l + j + 12, calificaciones[pos - 1][n]));
                    sheet.addCell(new Label(n + 8, l + j + 12, new DecimalFormat("#.#").format(registro[pos - 1] / encuestas)));
                    suma += Float.parseFloat(new DecimalFormat("#.#").format(registro[(pos) - 1] / encuestas));
                    entradas++;
                    pos++;
                    if (l == saltos[j + 1] - 3)
                        sheet.addCell(new Label(13, l + j + 13, new DecimalFormat("#.#").format(suma / entradas), new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));

                }
            } else {
                sheet.addCell(new Label(1, saltos[j] + 17, pos + " - " + preguntas[pos - 1]));
                int n;

                entradas++;
                suma += Float.parseFloat(new DecimalFormat("#.#").format(registro[(pos) - 1] / encuestas));

                for (n = 0; n < 5; n++)
                    sheet.addCell(new jxl.write.Number(n + 7, saltos[j] + 17, calificaciones[pos - 1][n]));
                sheet.addCell(new Label(13, saltos[j] + 17, new DecimalFormat("#.#").format(registro[pos - 1] / encuestas), new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));

            }
            for (int i = 0; i < criterios.length; i++) {
                if (i < criterios.length - 1)
                    sheet.addCell(new jxl.write.Number(i + 7, j + saltos[j]+11, i + 1, new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
                else
                    sheet.addCell(new Label(i + 7, j + saltos[j]+11, "CALIFICACIÓN", new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
            }
            calificacionFinal += (suma / entradas);
            suma = 0;
            entradas = 0;

        }

        sheet.addCell(new Label(1, 60, "CALIFICACIÓN FINAL", new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
        sheet.addCell(new Label(13, 61, new DecimalFormat("#.#").format(calificacionFinal / 6), new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.DOUBLE_ACCOUNTING, jxl.format.Colour.BLACK))));

        sheet.addCell(new Label(5, 1, instituto, new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
        sheet.addCell(new Label(4, 2, centro, new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
        sheet.addCell(new Label(5, 4, programaConstancia, new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
        sheet.addCell(new Label(4, 5, "INFORME DE EVALUACIÓN " + periodoInicio + " - " + periodoFin + " " + periodoAnio + " PROPEDÉUTICO", new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
        sheet.addCell(new Label(4, 8, "ASIGNATURA: " + asignatura, new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));
        sheet.addCell(new Label(4, 9, "PROFESOR: " + profesorConstancia, new WritableCellFormat(new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK))));

        workbook.write();
        workbook.close();
        download(archivo, "Ev_" + this.materiasActivas.toString() + "_" + this.materiasActivas.getNombreMateria() + "_" + this.materiasActivas.getSemestre() + ".xls");


    }

    public void generatedPDF() throws FileNotFoundException, DocumentException, IOException {


        con = 0;
        Document documento = new Document();
        File archivo = new File("Ev_" + this.materiasActivas.toString() + "_" + this.materiasActivas.getNombreMateria() + "_" + this.materiasActivas.getSemestre() + ".pdf");

        FileOutputStream ficheroPdf = new FileOutputStream(archivo, false);
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
        documento.open();

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setFont(FontFactory.getFont("arial",
                9,
                Font.BOLD,
                BaseColor.BLACK));
        paragraph.setIndentationLeft(20);
        paragraph.setIndentationRight(20);
        paragraph.add("INSTITUTO POLITÉCNICO NACIONAL \n" +
                "CENTRO DE INNOVACIÓN Y DESARROLLO TECNOLÓGICO DE CÓMPUTO \n" +
                programaConstancia + "\n" +
                "INFORME DE EVALUACIÓN " + periodoInicio + " - " + periodoFin + " " + periodoAnio + " PROPEDÉUTICO ");
        documento.add(paragraph);
        Paragraph texto1 = new Paragraph();
        texto1.setFont(FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK));
        texto1.add("La califiación se obtuvo promediando las respuestas ponderadas de la siguiente manera: ");
        Paragraph texto2 = new Paragraph();
        texto2.setFont(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));
        texto2.add("ASIGNATURA:  " + asignatura + "\n" +
                "PROFESOR:     " + profesorConstancia + "");
        Paragraph texto3 = new Paragraph();
        texto3.setFont(FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK));
        texto3.setIndentationLeft(130);
        texto3.setIndentationRight(20);
        texto3.add("PREGUNTA");

        PdfPTable tableCriterio = new PdfPTable(3);
        tableCriterio.setTotalWidth(Utilities.millimetersToPoints(90));
        tableCriterio.setLockedWidth(true);
        tableCriterio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        int tam = 5, posA = 0;
        tableCriterio.setTotalWidth(dimensionCriterio);
        for (int i = 0; i < 15; i++) {
            if (i % 3 == 0) {
                tableCriterio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableCriterio.addCell(new Paragraph("" + tam,
                        FontFactory.getFont("arial", 6, Font.NORMAL, BaseColor.BLACK)));
                tam--;
            } else {
                tableCriterio.addCell(new Paragraph("" + escala[posA],
                        FontFactory.getFont("arial", 6, Font.NORMAL, BaseColor.BLACK)));
                posA++;
            }

        }
        documento.add(texto1);
        documento.add(texto2);
        documento.add(tableCriterio);
        documento.add(texto3);


        int cont = 0;
        entradas = 0;
        suma = 0;
        calificacionFinal = 0;

        PdfPTable[] table = new PdfPTable[6];
        PdfPTable tableParcial = new PdfPTable(1);
        tableParcial.setTotalWidth(Utilities.millimetersToPoints(27.2f));
        tableParcial.setLockedWidth(true);
        tableParcial.setHorizontalAlignment(Element.ALIGN_RIGHT);


        for (int j = 0; j < indices.length; j++) {
            table[j] = new PdfPTable(7);
            table[j].setTotalWidth(pos);
            table[j].setWidthPercentage(100);
            for (int i = 0; i < indicesA[j]; i++) {
                if (i == 0) {
                    table[j].setTotalWidth(dimensiones);
                    table[j].addCell(new Paragraph(criterios[j],
                            FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
                } else if (i > 0 && i < 6) {
                    table[j].getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    table[j].addCell(new Paragraph("" + i,
                            FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));

                } else if (i == 6) {
                    table[j].addCell(new Paragraph("CALIFICACIÓN",
                            FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));

                } else {
                    if ((pos % 7) == 0) {
                        table[j].getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                        table[j].addCell(new Paragraph("" + pos / 7 + " " + preguntas[(pos / 7) - 1],
                                FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));

                    } else if (cont < 5) {
                        table[j].getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table[j].addCell(new Paragraph("" + calificaciones[(pos / 7) - 1][cont],
                                FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
                        cont++;
                    } else if (cont == 5) {
                        table[j].getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table[j].addCell(new Paragraph("" + new DecimalFormat("#.#").format(registro[(pos / 7) - 1] / encuestas),
                                FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
                        cont = 0;
                        suma += Float.parseFloat(new DecimalFormat("#.#").format(registro[(pos / 7) - 1] / encuestas));

                        entradas++;

                    } else {
                        table[j].getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table[j].addCell(new Paragraph("--",
                                FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
                    }
                    pos++;
                }
            }
            tableParcial.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableParcial.addCell(new Paragraph("" + new DecimalFormat("#.#").format((suma / entradas)),
                    FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
            pos = indices[j];
            calificacionFinal += (suma / entradas);
            suma = 0;
            entradas = 0;
            table[j].setSpacingBefore(8);
            documento.add(table[j]);
            documento.add(tableParcial);

            tableParcial.deleteRow(0);

        }

        PdfPTable tableCalifiacion = new PdfPTable(8);
        tableCalifiacion.setSpacingBefore(8);
        tableCalifiacion.setWidthPercentage(100);

        PdfPCell cell, cell1, cell2;
        cell = new PdfPCell(new Phrase(new Paragraph(" CALIFIACIÓN FINAL", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK))));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1 = new PdfPCell(new Phrase(""));
        cell2 = new PdfPCell(new Phrase(new Paragraph("" + new DecimalFormat("#.#").format(calificacionFinal / 6), FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK))));
        cell.setColspan(8);

        tableCalifiacion.addCell(cell);
        cell1.setColspan(7);

        tableCalifiacion.addCell(cell1);
        //cell2.setBorderWidth(2);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

        tableCalifiacion.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableCalifiacion.addCell(cell2);
        documento.add(tableCalifiacion);
        documento.close();
        download(archivo,"Ev_" + this.materiasActivas.toString() + "_" + this.materiasActivas.getNombreMateria() + "_" + this.materiasActivas.getSemestre() + ".pdf");

    }

    public String getSemestre(int semestre) {
        switch (semestre) {
            case 1:
                return "" + semestre + "er. Semestre";
            case 2:
                return "" + semestre + "do. Semestre";
            case 3:
                return "" + semestre + "ro. Semestre";
            case 4:
                return "" + semestre + "to. Semestre";
            case 5:
                return "" + semestre + "to. Semestre";
            case 6:
                return "" + semestre + "to. Semestre";
            case 7:
                return "" + semestre + "mo. Semestre";
            case 8:
                return "" + semestre + "vo. Semestre";
            case 9:
                return "" + semestre + "no. Semestre";
            default:
                return "no. Semestre";
        }
    }

    public float[] getListaMateriaEvaluada(String materia) {
        Query query = entityManager.createQuery("from EvaluacionRespuesta ev where ev.materia =:materia ").setParameter("materia", materia);
        List<EvaluacionRespuesta> lista = query.getResultList();
        float[] criteriosEvaluados = new float[7];
        int contador = 1, aux = 0, contIndices = 0, tam = lista.size() / 31;
        tamGeneral = tam;
        float evaluacion = 0;
        for (EvaluacionRespuesta evaluacionRespuesta : lista) {
            if (evaluacionRespuesta.getCriterio().getId() == contador && contador != 31) {
                aux += Integer.parseInt(evaluacionRespuesta.getRespuesta());
                if (contador == criteriosIndices[contIndices] ) {
                    criteriosEvaluados[contIndices] += aux;
                    aux = 0;
                    contIndices++;
                    if(contIndices > 5)
                        contIndices = 0;
                }

                contador++;
                if(contador > 30)
                    contador = 1;
            }
        }


        for(int a = 0; a < criteriosEvaluados.length-1; a++) {
            criteriosEvaluados[a] = Float.parseFloat(new DecimalFormat("#.#").format(Float.parseFloat(new DecimalFormat("#.#").format((criteriosEvaluados[a] / saltosIndices[a]))) / tam));
            evaluacion += criteriosEvaluados[a];
        }
        criteriosEvaluados[6] = Float.parseFloat(new DecimalFormat("#.#").format(evaluacion/6));
        return criteriosEvaluados;
    }

    public long getIdMateria(String materia){
        Query query = entityManager.createQuery(" select cu.id from Materia ma inner join ma.cursos cu where ma.clave = cu.materia.clave and ma.nombre =:materia").setParameter("materia", materia);
        long id = (Long)query.getResultList().get(0);
        return  id;
    }
    public MateriasActivas getMateriasActivasEvaluacion(EvaluacionRespuesta evaluacionRespuesta) {
        Query query = entityManager.createQuery(" select acc.id,c.id, m.nombre as nombreMateria, acc.nombre,acc.apellidoP,acc.apellidoM, c.semestre,c.grupo,p.nombre as nombrePrograma from Materia m inner join m.cursos c inner join m.programa p  inner join c.profesor acc where  m.clave = c.materia and acc.id = c.profesor.id and m.programa.id = p.id and c.id =:ID").setParameter("ID",getIdMateria(evaluacionRespuesta.getMateria()));
        List<MateriasActivas> materiasActivas1 = new ArrayList<MateriasActivas>();
        List<Object[]> materiasActivas = query.getResultList();
        MateriasActivas ma = new MateriasActivas();
        int i = 0;
        ma.setId(Long.parseLong(materiasActivas.get(i)[0].toString()));
        ma.setId_materia(Long.parseLong(materiasActivas.get(i)[1].toString()));
        ma.setNombreMateria(materiasActivas.get(i)[2].toString());
        ma.setNombre(materiasActivas.get(i)[3].toString());
        ma.setApellidoP(materiasActivas.get(i)[4].toString());
        ma.setApellidoM(materiasActivas.get(i)[5].toString());
        ma.setSemestre(materiasActivas.get(i)[6].toString());
        ma.setGrupo(materiasActivas.get(i)[7].toString());
        ma.setNombrePrograma(materiasActivas.get(i)[8].toString());
        return ma;
    }
    public boolean checkEvaluacion(long id) {

        Query query = entityManager.createQuery("from EvaluacionGeneral where curso.id =:id").setParameter("id",id);
        List<EvaluacionGeneral> lista = query.getResultList();
        for (EvaluacionGeneral evaluacionGeneral : lista)
            if (id == evaluacionGeneral.getCurso().getId())
                return true;
        return false;
    }
    public List<EvaluacionRespuesta> getListaMateriasEvaluadasUnicas(){
        Query query = entityManager.createQuery("from EvaluacionRespuesta ev group by ev.materia");
        return query.getResultList();
    }
    public boolean isEmpetyEvaluacionGeneral(){
        Query query = entityManager.createQuery("from EvaluacionGeneral ");
        return query.getResultList().size() > 0 ? false : true;
    }
    public long getIdMateria(){
        Query query = entityManager.createQuery("from Curso");
        List<Curso> lista = query.getResultList();
        for(Curso curso : lista)
            if(curso.getMateria().getNombre().equals(m.getNombre()))
                return curso.getId();
        return 0;
    }
    public Date periodoInicio() throws ParseException {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        if(query.getResultList().size() > 0) {
            return ((EvaluacionPeriodo) query.getResultList().get(0)).getPeriodo().getInicio();
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d2 = sdf.parse(String.valueOf(sdf.format(systemController.getPeriodoActual().getInicio())));
            return d2;
        }
    }
    public Date periodoTermino() throws ParseException {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        if(query.getResultList().size() > 0) {
            return ((EvaluacionPeriodo) query.getResultList().get(0)).getPeriodo().getTermino();
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d2 = sdf.parse(String.valueOf(sdf.format(systemController.getPeriodoActual().getTermino())));
            return d2;
        }
    }
    public int periodogetAnyo() throws ParseException {
        Query query = entityManager.createQuery("from EvaluacionPeriodo ");
        if(query.getResultList().size() > 0) {
            return ((EvaluacionPeriodo) query.getResultList().get(0)).getPeriodo().getAnyo();
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return systemController.getPeriodoActual().getAnyo();

        }
    }
    @End
    public String generalEvaluationReport() throws IOException, DocumentException, ParseException {
        if(getListaMateriasEvaluadasUnicas().size() > 0) {
            for (EvaluacionRespuesta evaluacionRespuesta : getListaMateriasEvaluadasUnicas()) {
                MateriasActivas materiasActivas = getMateriasActivasEvaluacion(evaluacionRespuesta);
                EvaluacionGeneral evaluacionGeneral = new EvaluacionGeneral();
                float[] criteriosIndividuales = getListaMateriaEvaluada(materiasActivas.getNombreMateria());
                m.setNombre(materiasActivas.getNombreMateria());
                m.setNombre(materiasActivas.getNombreMateria());
                c.setGrupo(getSemestre(Integer.parseInt(materiasActivas.getSemestre())));
                c.setMateria(m);
                c.setId(getIdMateria());
                evaluacionGeneral.setCurso(c);

                evaluacionGeneral.setSemestre_anio(letraCapital(String.valueOf(new SimpleDateFormat("MMM").format(periodoInicio()))) + " - " + letraCapital(String.valueOf(new SimpleDateFormat("MMM").format(periodoTermino()))) + "\n" + String.valueOf(periodogetAnyo()));
                evaluacionGeneral.setPuntualidad(criteriosIndividuales[0]);
                evaluacionGeneral.setDominio_asignatura(criteriosIndividuales[1]);
                evaluacionGeneral.setPrograma_impartido(criteriosIndividuales[2]);
                evaluacionGeneral.setEvaluacion_alumnos(criteriosIndividuales[3]);
                evaluacionGeneral.setActitud(criteriosIndividuales[4]);
                evaluacionGeneral.setOpinion_general(criteriosIndividuales[5]);
                evaluacionGeneral.setEvaluacion(criteriosIndividuales[6]);
                evaluacionGeneral.setAlumnos_evaluacion(tamGeneral);

                if (!checkEvaluacion(evaluacionGeneral.getCurso().getId())) {
                    entityManager.persist(evaluacionGeneral);
                    entityManager.flush();
                } else {
                    updateEvaluacionGeneral(evaluacionGeneral);
                }
            }
        }
        if(!isEmpetyEvaluacionGeneral()) {
            generatedGeneralEvaluationPDF();
            return "success";
        }else{
            return "failure";
        }

    }
    public String  letraCapital(String cadena){
        char[] caracteres = cadena.toCharArray();
        caracteres[0] = Character.toUpperCase(caracteres[0]);
        return new String(caracteres);
    }
    public String posicionAsignatura(String asignatura){
        String[] saltos = asignatura.split(" ");
        String arr = "";
        for(int i = 0; i < saltos.length; i++) {
            if (saltos[i].length() > 2)
                arr += saltos[i] + "\n";
            else
                if (saltos[i].length() > 2)
                    arr += " " + saltos[i] + "\n";
                else
                    arr += " " + saltos[i] + " ";
        }
        return arr;
    }
    @End
    public void updateEvaluacionGeneral(EvaluacionGeneral evaluacionGeneral){
        EvaluacionGeneral eva = entityManager.find(EvaluacionGeneral.class, findEvaluacionGeneralId(evaluacionGeneral));
        eva.setAlumnos_evaluacion(evaluacionGeneral.getAlumnos_evaluacion());
        eva.setActitud(evaluacionGeneral.getActitud());
        eva.setDominio_asignatura(evaluacionGeneral.getDominio_asignatura());
        eva.setOpinion_general(evaluacionGeneral.getOpinion_general());
        eva.setEvaluacion(evaluacionGeneral.getEvaluacion());
        eva.setPrograma_impartido(evaluacionGeneral.getPrograma_impartido());
        eva.setPuntualidad(evaluacionGeneral.getPuntualidad());
        entityManager.flush();
        systemController.reboot();

    }

    public int findEvaluacionGeneralId(EvaluacionGeneral evaluacionGeneral) {
        Query query = entityManager.createQuery("from EvaluacionGeneral eg where eg.curso.id =:evaluacion ").setParameter("evaluacion",evaluacionGeneral.getCurso().getId());
        return ((EvaluacionGeneral)query.getResultList().get(0)).getId();
    }
    public void download(File file,String fileName) throws IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        externalContext.setResponseHeader("Content-Type", "text/plain");
        externalContext.setResponseHeader("Content-Length", String.valueOf(file.length()));
        externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(file);
            output = externalContext.getResponseOutputStream();
            IOUtils.copy(input, output);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }

        facesContext.responseComplete();

    }

    public void generatedGeneralEvaluationPDF() throws IOException, DocumentException {
        Query query = entityManager.createQuery("from EvaluacionGeneral ");
        List<EvaluacionGeneral> lista = query.getResultList();

        File archivo = new File("Evaluacion.pdf");

        FileOutputStream ficheroPdf = new FileOutputStream(archivo, false);
        Document document
                = new Document(PageSize.POSTCARD, 0, 0, 78, 70);
        writer
                = PdfWriter.getInstance(document, ficheroPdf);
        writer.setCompressionLevel(0);

        FooterPdf footer = new FooterPdf();
        writer.setPageEvent(footer);
        document.open();
        PdfPTable tableCriterio = new PdfPTable(11);
        tableCriterio.setTotalWidth(Utilities.millimetersToPoints(90));

        int tam = 5, posA = 0;
        tableCriterio.setTotalWidth(dimensionHead);
        PdfPCell[] cel = new PdfPCell[11];

        for(int i = 0; i < header.length; i++){
            tableCriterio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell(new Phrase(header[i],
                    FontFactory.getFont("arial",4,Font.BOLD,BaseColor.BLACK)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(40);
            //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell.setVerticalAlignment(Element.ALIGN_CENTER);
            //cell.setLeading(-9, 5);
            if(i > 1){
                cell.setRotation(90);
                // cell.setLeading(1,1);
            }
            tableCriterio.addCell(cell);
        }
        tableCriterio.setHeaderRows(footer.getPageTotal());
        for(EvaluacionGeneral evaluacionGeneral : lista ){
            tableCriterio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableCriterio.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableCriterio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableCriterio.addCell(new Paragraph("" + evaluacionGeneral.getSemestre_anio(),
                    FontFactory.getFont("arial",4, Font.NORMAL, BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+letraCapital(posicionAsignatura(evaluacionGeneral.getCurso().getMateria().getNombre())),
                    FontFactory.getFont("arial",4, Font.NORMAL, BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getCurso().getSemestre(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getPuntualidad(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getDominio_asignatura(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getPrograma_impartido(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getEvaluacion_alumnos(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getActitud(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getOpinion_general(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getEvaluacion(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));
            tableCriterio.addCell(new Paragraph(""+evaluacionGeneral.getAlumnos_evaluacion(),
                    FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)));


        }
        document.add(tableCriterio);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Pagina "+writer.getPageNumber()+" de "+(footer.getPageTotal()+1),FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 150,300,0);
        document.close();
        download(archivo,"Evaluacion_General.pdf");
    }

}