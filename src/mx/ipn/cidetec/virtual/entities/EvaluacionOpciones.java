package mx.ipn.cidetec.virtual.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by sergio on 24/06/17.
 */
//@Entity
public class EvaluacionOpciones {
    private Long id;
    private EvaluacionPregunta pregunta;
    private String opcion;

//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)

}
