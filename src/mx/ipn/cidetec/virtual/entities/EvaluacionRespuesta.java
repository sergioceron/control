package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * Created by Usuario on 09/09/2014.
 */
@Entity
public class EvaluacionRespuesta {

    private Long id;
    private EvaluacionCriterio criterio;
    private int valor;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public EvaluacionCriterio getCriterio() {
        return criterio;
    }

    public void setCriterio(EvaluacionCriterio criterio) {
        this.criterio = criterio;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
