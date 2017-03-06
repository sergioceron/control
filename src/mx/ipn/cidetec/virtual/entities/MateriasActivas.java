package mx.ipn.cidetec.virtual.entities;

/**
 * Created by root on 6/01/16.
 */

public class MateriasActivas {

    private Long id;
    private Long id_materia;
    private String nombreMateria;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String semestre;
    private String grupo;
    private String nombrePrograma;

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId_materia() {
        return id_materia;
    }

    public void setId_materia(Long id_materia) {
        this.id_materia = id_materia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }
    public String toString(){
        return getApellidoP()+" "+getApellidoM()+" "+getNombre();
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getAbbr(){
        String[] palabras = nombrePrograma.split(" ");
        String abbr = new String();
        for (String palabra : palabras) {
            if( Character.isUpperCase( palabra.charAt(0) ) )
                abbr += palabra.charAt(0);
        }
        return abbr;
    }
}
