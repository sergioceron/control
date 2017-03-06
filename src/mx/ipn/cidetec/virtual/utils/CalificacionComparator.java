package mx.ipn.cidetec.virtual.utils;

import mx.ipn.cidetec.virtual.entities.Calificacion;

import java.util.Comparator;

/**
 * Created by sergio on 04/12/16.
 */
public class CalificacionComparator implements Comparator<Calificacion> {
    public int compare(Calificacion o1, Calificacion o2) {
        return o1.getCurso().getPeriodo().toStringOrder().compareTo(o2.getCurso().getPeriodo().toStringOrder());
    }
}