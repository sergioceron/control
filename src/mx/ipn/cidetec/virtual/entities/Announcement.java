package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by sergio on 04/10/15.
 */
@Entity
public class Announcement {
    private Long id;
    private String titulo;
    private List<Role> roles;
    private String contenido;
    private String estilo;
    private String url;
    private User autor;
    private Date inicio;
    private Date termino;
    private Long orden;
    private boolean sticky;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo( String titulo ) {
        this.titulo = titulo;
    }

    @ManyToMany
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles( List<Role> roles ) {
        this.roles = roles;
    }

    @Lob
    @Column( length = 100000 )
    public String getContenido() {
        return contenido;
    }

    public void setContenido( String contenido ) {
        this.contenido = contenido;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo( String estilo ) {
        this.estilo = estilo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public User getAutor() {
        return autor;
    }

    public void setAutor( User autor ) {
        this.autor = autor;
    }

    @Temporal( TemporalType.DATE)
    public Date getInicio() {
        return inicio;
    }

    public void setInicio( Date inicio ) {
        this.inicio = inicio;
    }

    @Temporal(TemporalType.DATE)
    public Date getTermino() {
        return termino;
    }

    public void setTermino( Date termino ) {
        this.termino = termino;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden( Long orden ) {
        this.orden = orden;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky( boolean sticky ) {
        this.sticky = sticky;
    }
}
